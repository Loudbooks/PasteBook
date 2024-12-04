package dev.loudbook.pastebook

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class PasteBookApplication

fun main(args: Array<String>) {
    val application = SpringApplication(PasteBookApplication::class.java)
    application.run(*args)
}