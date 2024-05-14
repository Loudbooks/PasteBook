package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.data.PastePrivateDTO
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface PasteRepository : MongoRepository<PastePrivateDTO, String> {
    @Query(value = "{}", fields = "{ 'content' : 0 }")
    fun findAllDTO(): List<PastePrivateDTO>

    @Query("{ 'id' : ?0 }", fields = "{ 'content' : 0 }")
    fun findDTOByID(id: String): PastePrivateDTO?
}