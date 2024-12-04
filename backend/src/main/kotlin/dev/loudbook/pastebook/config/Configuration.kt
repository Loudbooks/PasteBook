package dev.loudbook.pastebook.config

import dev.loudbook.pastebook.DeleteHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class Configuration {
    @Bean
    fun deleteHandler() = DeleteHandler()
}