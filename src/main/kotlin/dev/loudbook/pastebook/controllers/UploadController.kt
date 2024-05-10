package dev.loudbook.pastebook.controllers

import dev.loudbook.pastebook.BucketUtils
import dev.loudbook.pastebook.ContentScanner
import dev.loudbook.pastebook.Discord
import dev.loudbook.pastebook.data.PasteDTO
import dev.loudbook.pastebook.data.R2Service
import dev.loudbook.pastebook.mongo.PasteRepository
import io.github.bucket4j.Bucket
import jakarta.servlet.http.HttpServletRequest
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api")
class UploadController {
    @Autowired
    private lateinit var discord: Discord

    @Autowired
    lateinit var r2Service: R2Service

    @Autowired
    lateinit var pasteRepository: PasteRepository

    private val faker = Faker()
    private val bucket: Bucket = BucketUtils.getBucketPerMinutes(4)

    @PostMapping("/upload")
    fun upload(request: HttpServletRequest, @RequestBody body: String): ResponseEntity<String> {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body("Rate limit exceeded")
        }

        val header = HttpHeaders()

        var fileID = "${faker.cat().name().lowercase()}-${faker.dog().name().lowercase()}-${faker.horse().name().lowercase()}-${faker.food().ingredient().lowercase()}"
        fileID = fileID.replace(" ", "").replace("'", "").replace(",", "").replace(".", "").replace("(", "").replace(")", "")

        val start = Instant.now()
        val sinceTheEpoch = start.toEpochMilli()

        val title = request.getHeader("title") ?: return ResponseEntity.badRequest().body("Title is required")
        val reportBook = request.getHeader("reportBook")?.toBoolean() ?: false
        val wrap = request.getHeader("wrap")?.toBoolean() ?: false
        val unlisted = request.getHeader("unlisted")?.toBoolean() ?: false

        val filteredBody = ContentScanner.scanContent(body)

        val paste = PasteDTO(fileID, title, sinceTheEpoch, null, reportBook, unlisted, wrap)
        val pastebookURL = uploadPastebook(paste) ?: return ResponseEntity.badRequest().body("Failed to upload pastebook")

        val discordID = try {
            discord.send(title, pastebookURL, null).toLong()
        } catch (e: NullPointerException) {
            0L
        }

        paste.discordID = discordID

        r2Service.uploadFile(fileID, filteredBody)
        pasteRepository.save(paste)

        return ResponseEntity.ok().headers(header).body(pastebookURL)
    }

    fun uploadPastebook(paste: PasteDTO): String? {
        val url = "https://pastebook.dev/pastes/${paste.id}"
        return url
    }
}