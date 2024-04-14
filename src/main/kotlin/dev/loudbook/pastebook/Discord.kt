package dev.loudbook.pastebook

import com.google.gson.JsonParser
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

@Component
class Discord(private val properties: Properties) {
    fun send(name: String, url: String): String {
        val content = "New paste created: [${name}](${url})"

        val jsonStr = """
        {
            "content": "$content"
        }
    """.trimIndent()

        val discordURL = properties.getProperty("url")

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$discordURL?wait=true"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val json = response.body()
        val jsonObject = JsonParser.parseString(json).asJsonObject

        return jsonObject.get("id").asString
    }

    fun delete(id: String) {
        val discordURL = properties.getProperty("url")
        val deleteURL = "$discordURL/messages/$id"

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(deleteURL))
            .DELETE()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        println(response.body())
    }
}