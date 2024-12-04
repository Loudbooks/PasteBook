package dev.loudbook.pastebook.data

import org.springframework.data.annotation.Id

data class User(@Id val ip: String, val id: String, val requests: Int, val lastVisit: Long, val banned: Boolean) {
    fun incrementRequests() = User(ip, id, requests + 1, System.currentTimeMillis(), banned)
    fun toDTO() = UserDTO(id)
}