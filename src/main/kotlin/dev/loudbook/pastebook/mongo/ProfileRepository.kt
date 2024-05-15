package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.data.user.profile.Profile
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ProfileRepository : MongoRepository<Profile, String> {
    @Query("{ 'username' : ?0 }")
    fun findByUsername(username: String): Profile?

    @Query("{ 'email' : ?0 }")
    fun findByEmail(email: String): Profile?
}