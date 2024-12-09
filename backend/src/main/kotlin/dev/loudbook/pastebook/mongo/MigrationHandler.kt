package dev.loudbook.pastebook.mongo

import org.bson.Document
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

        val users = mongoTemplate.findAll(Document::class.java, "users")

        var result = 0

        users.forEach {
            val id = it.getString("id")
            if (id == null) {
                val newId = UUID.randomUUID().toString()
                val query = Query(Criteria.where("_id").`is`(it.getObjectId("_id")))
                val update = Update().set("id", newId)
                mongoTemplate.updateFirst(query, update, "users")
                logger.info("Fixed missing ID for user ${it.getString("ip")}")

                result++
            }
        }


        if (result > 0) {
            logger.info("Fixed ${result} missing IDs.")
        } else {
            logger.info("No missing IDs found.")
        }

        mongoTemplate.save(MigrationRecord("fixMissingIds"))
        logger.info("ID migration complete.")
    }
}

data class MigrationRecord(val migration: String)
