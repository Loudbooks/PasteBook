package dev.loudbook.pastebook

import com.github.javafaker.Faker
import dev.loudbook.pastebook.mongo.Paste
import dev.loudbook.pastebook.mongo.PasteRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class UploadController {
    @Autowired
    private lateinit var discord: Discord

    @Autowired
    lateinit var pasteRepository: PasteRepository

    val faker = Faker()

    @PostMapping("/upload")
    fun upload(request: HttpServletRequest, @RequestBody body: String): String? {
        var fileID = "${faker.cat().name().lowercase()}-${faker.dog().name().lowercase()}-${faker.music().instrument().lowercase()}-${faker.food().ingredient().lowercase()}"
        fileID = fileID.replace(" ", "")

        val start = Instant.now()
        val sinceTheEpoch = start.toEpochMilli()

        val url = "https://pastebook.dev/pastes/${fileID}"
        val title = request.getHeader("title") ?: return null

        val discordID = discord.send(title, url)

        val paste = Paste(fileID, title, body, sinceTheEpoch, discordID.toLong())
        pasteRepository.save(paste)

        return url
    }
}