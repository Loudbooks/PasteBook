package dev.loudbook.pastebook.data

import dev.loudbook.pastebook.data.user.UserDTO
import org.springframework.data.annotation.Id

@Suppress("unused")
class PasteDTO(@Id var id: String?, val user: UserDTO, val title: String, val created: Long, val reportBook: Boolean = false, val unlisted: Boolean = false, val wrap: Boolean = false)