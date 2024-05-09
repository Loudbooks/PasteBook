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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.OutputStream

@RestController
class GetController {
    @Autowired
    lateinit var pasteRepository: PasteRepository

    private val bucket: Bucket = BucketUtils.getBucketPerSeconds(4)

    @GetMapping("/get/{id}")
    fun get(@PathVariable id: String): ResponseEntity<StreamingResponseBody> {
        if (!bucket.tryConsume(1)) {
            val responseBody = StreamingResponseBody { outputStream: OutputStream ->
                outputStream.write("Rate limit exceeded".toByteArray())
            }

            return ResponseEntity.status(429).body(responseBody)
        }

        val paste = pasteRepository.findById(id).orElse(null) ?: return ResponseEntity.notFound().build()

        val headers = HttpHeaders()
        headers.add("title", paste.title)
        headers.add("reportBook", paste.reportBook.toString())
        headers.add("created", paste.created.toString())
        headers.add("wrap", paste.wrap.toString())

        val bufferedContentReader = paste.content.byteInputStream().bufferedReader()

        val responseBody = StreamingResponseBody { outputStream: OutputStream ->
            var numberOfBytesToWrite: Int
            val data = CharArray(1024)
            while ((bufferedContentReader.read(data, 0, data.size)
                    .also { numberOfBytesToWrite = it }) != -1
            ) {
                outputStream.write(String(data).encodeToByteArray(), 0, numberOfBytesToWrite)
            }
            bufferedContentReader.close()
        }

        return ResponseEntity.ok().headers(headers).body(responseBody)
    }
}