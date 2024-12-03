package dev.loudbook.pastebook.data

import org.apache.commons.codec.digest.DigestUtils
import org.springframework.data.annotation.Id

data class User(@Id val ip: String, val requests: Int, val lastVisit: Long, val banned: Boolean) {
    fun incrementRequests() = User(ip, requests + 1, System.currentTimeMillis(), banned)
    fun toDTO() = UserDTO(DigestUtils.sha256Hex(ip), requests, lastVisit, banned)
}