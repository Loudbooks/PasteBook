package dev.loudbook.pastebook.config

import dev.loudbook.pastebook.DeleteHandler
import dev.loudbook.pastebook.Discord
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Component
class Configuration {
    @Bean
    fun discord() = Discord(properties())

    @Bean
    fun deleteHandler() = DeleteHandler()

    fun properties(): Properties {
        val properties = Properties()
        properties.load(Files.newBufferedReader(Paths.get("./config.properties")))

        return properties
    }
}