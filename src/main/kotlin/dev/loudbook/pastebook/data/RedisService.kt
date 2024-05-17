package dev.loudbook.pastebook.data

import jakarta.annotation.PostConstruct
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService {
    private var redis: RedissonClient? = null

    @Value("\${redis.uri}")
    private val uri: String? = null

    @PostConstruct
    fun init() {
        try {
            val config = Config()
            config.useSingleServer().address = uri

            redis = Redisson.create(config)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cacheToken(token: String, id: String) {
        redis?.getBucket<String>(token)?.set(id, Duration.ofDays(7))
    }

    fun renewToken(token: String) {
        redis?.getBucket<String>(token)?.expire(Duration.ofDays(7))
    }

    fun getToken(token: String): String? {
        return redis?.getBucket<String>(token)?.get()
    }

    fun deleteToken(token: String) {
        redis?.getBucket<String>(token)?.delete()
    }
}