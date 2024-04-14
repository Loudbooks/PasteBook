package dev.loudbook.pastebook

import dev.loudbook.pastebook.mongo.PasteRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.concurrent.fixedRateTimer

@Component
class DeleteHandler {
    @Autowired
    val pasteRepository: PasteRepository? = null

    @PostConstruct
    fun init() {
        beginLoop()
    }

    private final fun beginLoop() {
        fixedRateTimer("timer", true, 1000, 1000 * 60 * 30) {
            deleteFiles()
        }
    }

    private fun deleteFiles() {
        val now = System.currentTimeMillis()
        val minimum = now - 1000 * 60 * 60 * 24 * 7

        val pastes = pasteRepository!!.findAfterTime(minimum)
        println("Deleting ${pastes.size} pastes")

        for (paste in pastes) {
            discord.delete(paste.discordID.toString())
            pasteRepository!!.delete(paste)
        }
    }
}