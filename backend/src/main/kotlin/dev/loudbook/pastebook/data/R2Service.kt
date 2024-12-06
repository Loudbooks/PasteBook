package dev.loudbook.pastebook.data

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI

@Service
class R2Service {
    private var amazonS3: S3Client? = null

    @Value("\${s3.accessKey}")
    private val accessKey: String? = null

    @Value("\${s3.secretKey}")
    private val secretKey: String? = null

    @Value("\${s3.url}")
    private val url: String? = null
    
    @Value("\${s3.bucket}")
    private val bucket: String? = null

    @PostConstruct
    fun init() {
        try {
            amazonS3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(AwsCredentialsProvider { AwsBasicCredentials.create(accessKey, secretKey) })
                .endpointOverride(URI.create(url!!))
                .serviceConfiguration(S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build())
                .build()

            println("Connected to S3, found buckets: ${amazonS3?.listBuckets()?.buckets()?.map { it.name() }}")

            println("Creating bucket $bucket")

            if (!amazonS3?.listBuckets()?.buckets()?.any { it.name() == bucket }!!) {
                amazonS3?.createBucket(CreateBucketRequest.builder().bucket(bucket).build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun uploadFile(key: String, paste: String) {
        amazonS3?.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromString(paste))
    }

    fun deleteFile(key: String) {
        amazonS3?.deleteObject { it.bucket(bucket).key(key) }
    }

    fun getFile(key: String): String? {
        val str = amazonS3?.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build())?.readAllBytes()
            ?.toString(Charsets.UTF_8)

        return str
    }

    fun listFileNames(): List<String> {
        return amazonS3?.listObjects(ListObjectsRequest.builder().bucket(bucket).build())?.contents()?.map { it.key() } ?: emptyList()
    }
}