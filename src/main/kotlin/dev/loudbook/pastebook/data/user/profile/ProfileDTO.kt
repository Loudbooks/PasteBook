package dev.loudbook.pastebook.data.user.profile

data class ProfileDTO(
    val token: String,
    val identification: String,
    val username: String) {

    fun toJson(): String {
        return """
            {
                "token": "$token",
                "identification": "$identification",
                "username": "$username"
            }
        """.trimIndent()
    }
}