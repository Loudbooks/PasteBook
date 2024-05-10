package dev.loudbook.pastebook.data

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.google.gson.Gson
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class R2Service {
    private var amazonS3: AmazonS3? = null

    @Value("\${s3.accessKey}")
    private val accessKey: String? = null

    @Value("\${s3.secretKey}")
    private val secretKey: String? = null

    @Value("\${s3.url}")
    private val url: String? = null

    @PostConstruct
    fun init() {
        try {
            amazonS3 = AmazonS3ClientBuilder.standard().apply {
                setEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(url, "us-east-1"))
            }

                .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun uploadFile(key: String, paste: String) {
        amazonS3?.putObject("pastebook", key, paste)
    }

    fun deleteFile(key: String) {
        amazonS3?.deleteObject("pastebook", key)
    }

    fun getFile(key: String): String? {
        val str = amazonS3?.getObject("pastebook", key)?.objectContent?.readAllBytes()?.toString(Charsets.UTF_8)

        return str
    }

    fun fileExists(key: String): Boolean {
        return amazonS3?.doesObjectExist("pastebook", key) ?: false
    }
}