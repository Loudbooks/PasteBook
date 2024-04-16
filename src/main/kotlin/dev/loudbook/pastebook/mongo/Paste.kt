package dev.loudbook.pastebook.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Suppress("unused")
@Document("paste")
class Paste(@Id var id: String?, val title: String, val content: String, val created: Long, var discordID : Long?, val reportBook: Boolean)