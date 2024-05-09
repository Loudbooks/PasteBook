package dev.loudbook.pastebook.controllers

import com.google.gson.Gson
import dev.loudbook.pastebook.BucketUtils
import dev.loudbook.pastebook.mongo.PasteRepository
import io.github.bucket4j.Bucket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetController {
    @Autowired
    lateinit var pasteRepository: PasteRepository

    private val bucket: Bucket = BucketUtils.getBucketPerSeconds(4)

    @GetMapping("/get/{id}")
    fun get(@PathVariable id: String): ResponseEntity<String> {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body("Rate limit exceeded")
        }

        val paste = pasteRepository.findById(id).orElse(null) ?: return ResponseEntity.notFound().build()

        val headers = HttpHeaders()
        headers.add("Access-Control-Allow-Origin", "*")
        headers.add("title", paste.title)
        headers.add("reportBook", paste.reportBook.toString())
        headers.add("created", paste.created.toString())

        return ResponseEntity.ok().headers(headers).body(Gson().toJson(paste))
    }
}