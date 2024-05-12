package dev.loudbook.pastebook

import dev.loudbook.pastebook.data.PasteDTO
import dev.loudbook.pastebook.data.R2Service
import dev.loudbook.pastebook.mongo.PasteRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.concurrent.fixedRateTimer

@Component
class DeleteHandler {
    @Autowired
    val r2Service: R2Service? = null

    @Autowired
    lateinit var pasteRepository: PasteRepository

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
        val minimum = now - 1000 * 60 * 60 * 9
        println("Minimum time: $minimum")

        val deletablePastes = mutableListOf<PasteDTO>()

        var index = 0

        pasteRepository.findAllDTO().forEach {
            println("Comparing $minimum with ${it.created}")
            index++

            println(minimum > it.created)

            if (it.created < minimum) {
                deletablePastes.add(it)
            }
        }

        for (paste in deletablePastes) {
            if (!paste.unlisted) {
                discord.delete(paste.discordID.toString())
            }

            pasteRepository.delete(paste)
            paste.id?.let { r2Service?.deleteFile(it) }
        }
    }
}