package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.data.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UsersRepository : MongoRepository<User, String> {
    @Query("{ 'ip' : ?0 }")
    fun findByIp(ip: String): User?
}