package dev.loudbook.pastebook.data.user.profile

import org.springframework.data.annotation.Id

data class Profile(
    @Id val identifier: String,
    val username: String,
    val password: ByteArray?,
    val salt: String?,
    val oAuth: Boolean,
    val verified: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Profile

        if (username != other.username) return false
        if (identifier != other.identifier) return false
        if (!password.contentEquals(other.password)) return false
        if (salt != other.salt) return false
        if (oAuth != other.oAuth) return false
        if (verified != other.verified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + identifier.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + salt.hashCode()
        return result
    }
}
