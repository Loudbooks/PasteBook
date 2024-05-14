package dev.loudbook.pastebook.data

import org.springframework.data.annotation.Id

@Suppress("unused")
class PasteDTO(@Id var id: String?, val user: UserDTO, val title: String, val created: Long, val reportBook: Boolean = false, val unlisted: Boolean = false, val wrap: Boolean = false)