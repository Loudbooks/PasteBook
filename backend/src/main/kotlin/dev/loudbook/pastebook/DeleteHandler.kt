package dev.loudbook.pastebook

import dev.loudbook.pastebook.data.PastePrivateDTO
import dev.loudbook.pastebook.data.R2Service
import dev.loudbook.pastebook.mongo.PasteRepository
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.concurrent.fixedRateTimer

@Component
class DeleteHandler {
    @Autowired
    lateinit var r2Service: R2Service

    @Autowired
    lateinit var pasteRepository: PasteRepository

    private val logger: Logger = LoggerFactory.getLogger(DeleteHandler::class.java)

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
            pasteRepository.delete(paste)
            paste.id?.let { r2Service.deleteFile(it) } ?: logger.error("Failed to delete paste; $paste")
        }

        for (listFileName in r2Service.listFileNames()) {
            if (allPastes.none{ it.id == listFileName}) {
                r2Service.deleteFile(listFileName)
                logger.warn("Deleted invalid file $listFileName")
            }
        }
    }
}