package dev.loudbook.pastebook.data

data class UserDTO(val hashedIP: String, val requests: Int, val lastVisit: Long, val banned: Boolean)