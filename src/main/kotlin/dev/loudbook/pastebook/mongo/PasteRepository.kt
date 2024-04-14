package dev.loudbook.pastebook.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface PasteRepository : MongoRepository<Paste, String> {
    @Query("{ 'created' : { \$lt : ?0 } }")
    fun findAfterTime(created: Long): List<Paste>
}