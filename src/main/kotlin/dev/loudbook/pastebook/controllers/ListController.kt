package dev.loudbook.pastebook.controllers

import com.google.gson.Gson
import com.google.gson.JsonParser
import dev.loudbook.pastebook.mongo.PasteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ListController {
    @Autowired
    lateinit var pasteRepository: PasteRepository

    @GetMapping("/list")
    fun list(): String {
        val pastes = pasteRepository.findAll().toList()
        val json = JsonParser.parseString(Gson().toJson(pastes)).asJsonArray

        json.mapIndexed { _, element ->
            element.asJsonObject.remove("content")
        }

        return json.toString()
    }
}