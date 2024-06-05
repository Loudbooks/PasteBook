package dev.loudbook.pastebook

import dev.loudbook.pastebook.data.PastePrivateDTO
import dev.loudbook.pastebook.data.R2Service
import dev.loudbook.pastebook.mongo.PasteRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.concurrent.fixedRateTimer

@Component
class DeleteHandler {
    @Autowired
    lateinit var r2Service: R2Service

    @Autowired
    lateinit var pasteRepository: PasteRepository

    @PostConstruct
    fun init() {
        beginLoop()
    }

    private final fun beginLoop() {
        fixedRateTimer("timer", true, 1000, 1000 * 60 * 10) {
            deleteFiles()
        }
    }

    private fun deleteFiles() {
        val deletablePastes = mutableListOf<PastePrivateDTO>()
        val allPastes = mutableListOf<PastePrivateDTO>()

        var index = 0

        pasteRepository.findAllDTO().forEach {
            val expires = it.expires
            index++

            allPastes.add(it)
            
            if (System.currentTimeMillis() > expires && expires != 0L) {
                deletablePastes.add(it)
            }
        }

        for (paste in deletablePastes) {
            if (!paste.unlisted) {
                discord.delete(paste.discordID.toString())
            }

            pasteRepository.delete(paste)
            paste.id?.let { r2Service.deleteFile(it) } ?: println("Failed to delete paste; $paste")
        }

        for (listFileName in r2Service.listFileNames()) {
            if (allPastes.none{ it.id == listFileName}) {
                r2Service.deleteFile(listFileName)
                println("Deleted invalid file $listFileName")
            }
        }
    }
}