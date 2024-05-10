package dev.loudbook.pastebook.data

import org.springframework.data.annotation.Id

class PasteDTO(@Id var id: String?, val title: String, val created: Long, var discordID: Long?, val reportBook: Boolean = false, val unlisted: Boolean = false, val wrap: Boolean = false)