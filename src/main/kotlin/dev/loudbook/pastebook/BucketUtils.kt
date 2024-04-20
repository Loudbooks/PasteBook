package dev.loudbook.pastebook

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import java.time.Duration


object BucketUtils {
    fun getBucketPerSeconds(perSeconds: Long): Bucket {
        val limit = Bandwidth.classic(perSeconds, Refill.greedy(perSeconds, Duration.ofSeconds(1)))
        return Bucket.builder()
            .addLimit(limit)
            .build()
    }

    fun getBucketPerMinutes(perMinutes: Long): Bucket {
        val limit = Bandwidth.classic(perMinutes, Refill.greedy(perMinutes, Duration.ofMinutes(1)))
        return Bucket.builder()
            .addLimit(limit)
            .build()
    }
}