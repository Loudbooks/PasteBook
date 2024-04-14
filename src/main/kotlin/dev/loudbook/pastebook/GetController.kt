package dev.loudbook.pastebook

import com.google.gson.Gson
import dev.loudbook.pastebook.mongo.PasteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetController {
    @Autowired
    lateinit var pasteRepository: PasteRepository

    @GetMapping("/get/{id}")
    fun get(@PathVariable id: String): String {
        val paste = pasteRepository.findById(id).orElse(null) ?: return ""

        return Gson().toJson(paste)
    }
}