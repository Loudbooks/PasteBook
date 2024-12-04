package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.IPUtils
import dev.loudbook.pastebook.data.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService {
    @Autowired
    private lateinit var usersRepository: UsersRepository

    fun incrementRequests(ip: String) {
        val user = usersRepository.findByIp(ip)
        if (user != null) {
            val newUser = user.incrementRequests()
            usersRepository.save(newUser)
        } else {
            val userID = UUID.randomUUID().toString()
            usersRepository.save(User(ip, userID, 1, System.currentTimeMillis(), false))
        }
    }

    fun processRequest(request: HttpServletRequest): Boolean {
        val ip = IPUtils.getIPFromRequest(request) ?: return false
        val user = usersRepository.findByIp(ip)
        if (user != null && user.banned) {
            return false
        }

        incrementRequests(ip)
        return true
    }

    fun getUser(ip: String): User? {
        return usersRepository.findByIp(ip)
    }
}