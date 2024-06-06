package dev.loudbook.pastebook

import com.google.gson.JsonParser
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import kotlin.concurrent.schedule

@Component
class Discord(private val properties: Properties) {
    init {
        send("WE TESTING", "STILL TESTING", null, 1717752907981)
    }

    final fun send(name: String, pastebookURL: String, pasteGGURL: String?, expireTime: Long): String {
        val content = if (pasteGGURL != null) {
            "New paste created (${name}): [PasteBook](<${pastebookURL}>) | [PasteGG](${pasteGGURL})\\n\\nExpires <t:$expireTime:R>"
        } else {
             "New paste created: [${name}](<${pastebookURL}>)\\n\\nExpires <t:$expireTime:R>"
        }

        val jsonStr = """
        {
            "content": "$content",
            "allowed_mentions": {
                "parse": []
            }
        }
    """.trimIndent()

        val discordURL = properties.getProperty("url")
        println(jsonStr)

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

        try {
            val jsonResponse = JsonParser.parseString(response.body()).asJsonObject
            val message = jsonResponse.get("message").asString

            if (message != null && message.contains("rate limited")) {
                println("Rate limited, retrying in 3 seconds")
                Timer().schedule(3500) {
                    println("Retrying delete of $id")
                    delete(id)
                }
            }
        } catch (e: IllegalStateException) {
            println("Failed to delete $id")
        } catch (e: NullPointerException) {
            println("Failed to delete $id")
        }

        println(response.body())
    }
}