package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.data.PasteDTO
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface PasteRepository : MongoRepository<PasteDTO, String> {
    @Query("{ 'created' : { \$lt : ?0 } }")
    fun findAfterTime(created: Long): List<PasteDTO>

    @Query(value = "{}", fields = "{ 'content' : 0 }")
    fun findAllDTO(): List<PasteDTO>

    @Query("{ 'id' : ?0 }", fields = "{ 'content' : 0 }")
    fun findDTOByID(id: String): PasteDTO?

    @Query("{ 'id' : ?0 }", fields = "{ 'content' : 1 }")
    fun findContentByID(id: String): PasteDTO?
}