package dev.loudbook.pastebook

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.function.Supplier


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
@EnableMongoRepositories
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
}