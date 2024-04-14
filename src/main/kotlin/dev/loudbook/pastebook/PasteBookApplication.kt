package dev.loudbook.pastebook

import dev.loudbook.pastebook.mongo.PasteRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.function.Supplier
import kotlin.concurrent.fixedRateTimer

@SpringBootApplication
@EnableMongoRepositories
class PasteBookApplication {
    @Autowired
    lateinit var pasteRepository: PasteRepository

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
    }

    @PostConstruct
    fun init() {
        fixedRateTimer("timer", true, 1000, 1000 * 60 * 30) {
            deleteFiles()
        }
    }

    fun deleteFiles() {
        val now = System.currentTimeMillis()
        val minimum = now - 1000 * 60 * 60 * 24 * 7

        val pastes = pasteRepository.findAfterTime(minimum)

        for (paste in pastes) {
            discord.delete(paste.discordID.toString())
        }
    }
}