package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.data.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MigrationHandler(private val mongoTemplate: MongoTemplate) : CommandLineRunner {

    private val logger: Logger = LoggerFactory.getLogger(MigrationHandler::class.java)

    override fun run(vararg args: String?) {
        idMigration(mongoTemplate)
    }

    fun idMigration(mongoTemplate: MongoTemplate) {
        val migrationQuery = Query(Criteria.where("migration").`is`("fixMissingIds"))
        val existingMigration = mongoTemplate.findOne(migrationQuery, MigrationRecord::class.java)

        if (existingMigration != null) {
            logger.info("ID migration already completed.")
            return
        }

        logger.info("Beginning migration of missing IDs...")

        val query = Query(Criteria.where("id").exists(false))
        val update = Update().set("id", UUID.randomUUID().toString())
        val result = mongoTemplate.updateMulti(query, update, User::class.java)

        if (result.modifiedCount > 0) {
            logger.info("Fixed ${result.modifiedCount} missing IDs.")
        } else {
            logger.info("No missing IDs found.")
        }

        mongoTemplate.save(MigrationRecord("fixMissingIds"))
        logger.info("ID migration complete.")
    }
}

data class MigrationRecord(val migration: String)
