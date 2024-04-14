package dev.loudbook.pastebook

import com.google.gson.JsonParser
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.function.Supplier
import kotlin.concurrent.fixedRateTimer

const val PATH: String = "/home/loudbook/pastebook/pastebook"
const val PASTE_PATH: String = "$PATH/pastes"


@SpringBootApplication
class PasteBookApplication

val discord = Discord(Properties().apply {
    load(Files.newBufferedReader(Paths.get("./config.properties")))
})

fun main(args: Array<String>) {
    val application = SpringApplication(PasteBookApplication::class.java)

    application.addInitializers(ApplicationContextInitializer { ctx: GenericApplicationContext ->
        ctx.registerBean(
            Discord::class.java,
            Supplier { discord })
    })

    application.run(*args)

    fixedRateTimer("timer", true, 1000, 1000 * 60 * 30) {
        deleteFiles()
    }
}

fun deleteFiles() {
    val path = Paths.get(PASTE_PATH)
    val files = Files.list(path).toList()

    for (file in files) {
        val content = Files.readString(file)
        if (content.isEmpty()) continue
        val json = JsonParser.parseString(content)?.asJsonObject ?: continue

        val created = json.get("created")?.asLong ?: continue
        val now = System.currentTimeMillis()
        val diff = now - created

        if (diff < 1000 * 60 * 60 * 9) continue

        val id = json.get("id")?.asString ?: continue

        discord.delete(id)
        Files.delete(file)
    }
}