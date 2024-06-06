package dev.loudbook.pastebook.controllers

import dev.loudbook.pastebook.BucketUtils
import dev.loudbook.pastebook.ContentScanner
import dev.loudbook.pastebook.Discord
import dev.loudbook.pastebook.IPUtils
import dev.loudbook.pastebook.data.PastePrivateDTO
import dev.loudbook.pastebook.data.R2Service
import dev.loudbook.pastebook.mongo.PasteRepository
import dev.loudbook.pastebook.mongo.UserService
import io.github.bucket4j.Bucket
import jakarta.servlet.http.HttpServletRequest
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import kotlin.math.exp

@RestController
class UploadController {
    @Autowired
    private lateinit var discord: Discord

    @Autowired
    lateinit var r2Service: R2Service

    @Autowired
    lateinit var pasteRepository: PasteRepository

    @Autowired
    lateinit var userService: UserService

    private val faker = Faker()
    private val bucket: Bucket = BucketUtils.getBucketPerMinutes(4)

    @PostMapping(value = ["/api/upload", "/upload"])
    fun upload(request: HttpServletRequest, @RequestBody body: String): ResponseEntity<String> {
        if (!userService.processRequest(request)) {
            return ResponseEntity.status(403).body("Prohibited")
        }

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body("Rate limit exceeded")
        }

        val header = HttpHeaders()

        var fileID = "${faker.cat().name().lowercase()}-${faker.dog().name().lowercase()}-${faker.horse().name().lowercase()}-${faker.food().ingredient().lowercase()}"
        fileID = fileID.replace(" ", "").replace("'", "").replace(",", "").replace(".", "").replace("(", "").replace(")", "")

        val sinceTheEpoch = System.currentTimeMillis()

        val title = request.getHeader("title") ?: return ResponseEntity.badRequest().body("Title is required")
        val reportBook = request.getHeader("reportBook")?.toBoolean() ?: false
        val wrap = request.getHeader("wrap")?.toBoolean() ?: false
        val unlisted = request.getHeader("unlisted")?.toBoolean() ?: false
        var expire = request.getHeader("expires")?.toLong() ?: (sinceTheEpoch + 8.64e+7).toLong()

        if (expire < 60000) {
            return ResponseEntity.badRequest().body("Expire time too short")
        }

        if (expire < sinceTheEpoch) {
            expire += sinceTheEpoch
        }

        if (expire > (sinceTheEpoch + 2.765e+9)) {
            return ResponseEntity.badRequest().body("Expire time too long")
        }

        val filteredBody = ContentScanner.scanContent(body)

        val ip = IPUtils.getIPFromRequest(request) ?: return ResponseEntity.badRequest().body("Failed to get IP")

        val paste = PastePrivateDTO(fileID, title, sinceTheEpoch, null, reportBook, unlisted, wrap, ip, expire)
        val pastebookURL = uploadPastebook(paste) ?: return ResponseEntity.badRequest().body("Failed to upload pastebook")

        val discordID = try {
            if (!unlisted) {
                discord.send(title, pastebookURL, null).toLong()
            } else {
                0L
            }
        } catch (e: NullPointerException) {
            0L
        }

        paste.discordID = discordID

        r2Service.uploadFile(fileID, filteredBody)
        pasteRepository.save(paste)

        return ResponseEntity.ok().headers(header).body(pastebookURL)
    }

    fun uploadPastebook(paste: PastePrivateDTO): String? {
        val url = "https://pastebook.dev/pastes/${paste.id}"
        return url
    }
}