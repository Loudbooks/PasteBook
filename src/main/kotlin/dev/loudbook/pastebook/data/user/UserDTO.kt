package dev.loudbook.pastebook.data.user

data class UserDTO(val hashedIP: String, val requests: Int, val lastVisit: Long, val banned: Boolean)