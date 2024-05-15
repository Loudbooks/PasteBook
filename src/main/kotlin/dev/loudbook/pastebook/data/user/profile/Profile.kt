package dev.loudbook.pastebook.data.user.profile

data class Profile(
    val username: String,
    val email: String,
    val password: String,
    val salt: String,
)
