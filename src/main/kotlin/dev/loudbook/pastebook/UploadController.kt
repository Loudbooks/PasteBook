package dev.loudbook.pastebook

import com.github.javafaker.Faker
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant

@RestController
class UploadController {
    @Autowired
    private lateinit var discord: Discord

    val faker = Faker()

    @PostMapping("/upload")
    fun upload(request: HttpServletRequest, @RequestBody body: String): String? {
        val dataDir = "$PATH/pastes/"
        var filename = "${faker.cat().name().lowercase()}-${faker.dog().name().lowercase()}-${faker.music().instrument().lowercase()}-${faker.food().ingredient().lowercase()}"
        filename = filename.replace(" ", "")
        val path = "${dataDir}/${filename}.json"

        if (!Files.exists(Paths.get(dataDir))) {
            Files.createDirectories(Paths.get(dataDir))
        }

        val start = Instant.now()
        val sinceTheEpoch = start.toEpochMilli()

        val url = "https://pastebook.dev/pastes/${filename}"
        val title = request.getHeader("title") ?: return null

        val id = discord.send(title, url)

        val json = """
            {
                "title": "$title",
                "content": "$body",
                "id": "$id",
                "created": "$sinceTheEpoch"
            }
        """.trimIndent()

        Files.write(Paths.get(path), json.toByteArray())

        return url
    }
}