package dev.loudbook.pastebook.data.user.profile

enum class ProfileResult {
    SUCCESS,
    USERNAME_TAKEN,
    EMAIL_TAKEN,
    USERNAME_NOT_FOUND,
    INCORRECT_PASSWORD,
    INVALID_EMAIL
}