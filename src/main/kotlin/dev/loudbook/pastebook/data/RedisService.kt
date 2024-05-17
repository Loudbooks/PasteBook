package dev.loudbook.pastebook.data

import io.lettuce.core.RedisClient
import io.lettuce.core.api.async.RedisAsyncCommands
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService {
    private var redis: RedisClient? = null
    private var asyncCommands: RedisAsyncCommands<String, String>? = null

    @Value("\${redis.uri}")
    private val uri: String? = null

    @PostConstruct
    fun init() {
        try {
            redis = RedisClient.create(uri)

            asyncCommands = redis?.connect()?.async()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cacheToken(token: String, id: String) {
        asyncCommands?.set(token, id)
        asyncCommands?.expire(token, Duration.ofDays(7))
    }

    fun renewToken(token: String) {
        asyncCommands?.expire(token, Duration.ofDays(7))
    }

    fun getToken(token: String): String? {
        return asyncCommands?.get(token)?.get()
    }

    fun deleteToken(token: String) {
        asyncCommands?.del(token)
    }
}