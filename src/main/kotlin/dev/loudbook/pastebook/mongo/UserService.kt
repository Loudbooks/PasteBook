package dev.loudbook.pastebook.mongo

import dev.loudbook.pastebook.IPUtils
import dev.loudbook.pastebook.data.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
            usersRepository.save(User(ip, 1, System.currentTimeMillis(), false))
            println("New user: $ip")
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
}