package dev.loudbook.pastebook

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class GetController {
    @GetMapping("/get/{id}")
    fun get(@PathVariable id: String): String {
        val path = "${PASTE_PATH}/${id}.json"
        val stringContent = Files.readString(Paths.get(path))
        val jsonObject = JsonParser.parseString(stringContent)?.asJsonObject ?: return ""

        val responseJson = Gson().toJson(
            mapOf(
                "content" to jsonObject.get("content")?.asString,
                "title" to jsonObject.get("title")?.asString,
                "created" to jsonObject.get("created")?.asString
            )
        ) ?: return ""

        return responseJson
    }
}