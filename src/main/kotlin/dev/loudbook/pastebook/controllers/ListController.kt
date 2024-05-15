package dev.loudbook.pastebook.controllers

import com.google.gson.Gson
import dev.loudbook.pastebook.BucketUtils
import dev.loudbook.pastebook.data.user.UserDTO
import dev.loudbook.pastebook.mongo.PasteRepository
import dev.loudbook.pastebook.mongo.UserService
import io.github.bucket4j.Bucket
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ListController {
    @Autowired
    lateinit var pasteRepository: PasteRepository

    @Autowired
    lateinit var userService: UserService

    private val bucket: Bucket = BucketUtils.getBucketPerSeconds(4)

    @GetMapping("/list")
    fun list(request: HttpServletRequest): ResponseEntity<String> {
        if (!userService.processRequest(request)) {
            return ResponseEntity.status(403).body("Prohibited")
        }

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body("Rate limit exceeded")
        }

        val users = pasteRepository.findAllDTO().map { it.creatorIP }.toMutableList()
        val ipToUsers: Map<String, UserDTO> = users
            .filter { userService.getUser(it) != null }
            .associateWith { userService.getUser(it)!!.toDTO() }

        val userPastes = pasteRepository.findAllDTO()
            .asSequence()
            .filter { it.creatorIP in users && ipToUsers.containsKey(it.creatorIP) && it.created < System.currentTimeMillis() && !it.unlisted }
            .map { it.toPublicDTO(ipToUsers[it.creatorIP]!!) }
            .take(80)
            .toList()

        val json = Gson().toJsonTree(userPastes).asJsonArray

        json.mapIndexed { _, element ->
            element.asJsonObject.remove("content")
        }

        return ResponseEntity.ok(json.toString())
    }
}