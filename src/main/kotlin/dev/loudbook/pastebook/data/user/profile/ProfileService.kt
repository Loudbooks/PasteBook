package dev.loudbook.pastebook.data.user.profile

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.loudbook.pastebook.mongo.ProfileRepository
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class ProfileService {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    private lateinit var key: String

    init {
        val file = Paths.get("./sha.key")
        if (Files.exists(file)) {
            key = String(Files.readAllBytes(file))
        } else {
            throw Exception("Key file not found")
        }
    }

    fun processLogin(identification: String, password: String): Result<String> {
        val profile = findPossibleProfile(identification)
            ?: return Result.failure(ProfileException(ProfileResult.USERNAME_NOT_FOUND))

        val attemptedPasswordWithSalt = saltPassword(password, profile.salt)

        if (!saltPassword(profile.password, profile.salt).contentEquals(attemptedPasswordWithSalt)) {
            return Result.failure(ProfileException(ProfileResult.INCORRECT_PASSWORD))
        }

        return Result.success(generateToken(profile))
    }

    fun processAccountCreation(username: String, email: String, hashedPassword: String, salt: String): Result<String> {
        if (findPossibleProfile(username) != null) {
            return Result.failure(ProfileException(ProfileResult.USERNAME_TAKEN))
        }

        if (findPossibleProfile(email) != null) {
            return Result.failure(ProfileException(ProfileResult.EMAIL_TAKEN))
        }

        profileRepository.save(Profile(username, email, hashedPassword, salt))
        val token = generateToken(Profile(username, email, hashedPassword, salt))

        return Result.success(token)
    }

    fun generateToken(profile: Profile): String {
        return JWT.create()
            .withClaim("username", profile.username)
            .withClaim("email", profile.email)
            .sign(Algorithm.HMAC256(key))
    }

    fun validateToken(token: String): Boolean {
        val verifier = JWT.require(Algorithm.HMAC256(key)).build()
        val decoded = verifier.verify(token)

        return profileRepository.findByEmail(decoded.getClaim("email").asString()) != null
    }

    fun findPossibleProfile(identification: String): Profile? {
        return profileRepository.findByUsername(identification) ?: profileRepository.findByEmail(identification)
    }

    private fun saltPassword(password: String, salt: String): ByteArray {
        return DigestUtils.sha256(password + salt)
    }
}