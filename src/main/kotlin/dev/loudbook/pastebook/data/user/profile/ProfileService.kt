package dev.loudbook.pastebook.data.user.profile

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import dev.loudbook.pastebook.mongo.ProfileRepository
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.keygen.KeyGenerators
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class ProfileService {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    private lateinit var key: String

    init {
        val file = Paths.get("./id_rsa")
        if (Files.exists(file)) {
            key = String(Files.readAllBytes(file))
            println("Prepared key file for JWT signing")
        } else {
            throw Exception("Key file not found")
        }
    }

    fun processLogin(identification: String, password: String? = null): Result<String> {
        val profile = findPossibleProfile(identification)
            ?: return Result.failure(ProfileException(ProfileResult.USERNAME_NOT_FOUND))

        if (password != null && !profile.oAuth && profile.salt != null) {
            val attemptedPasswordWithSalt = saltPassword(password, profile.salt)

            if (!profile.password.contentEquals(attemptedPasswordWithSalt)) {
                return Result.failure(ProfileException(ProfileResult.INCORRECT_PASSWORD))
            }
        }

        return Result.success(generateToken(profile))
    }

    fun processAccountCreation(username: String, email: String?, hashedPassword: String?, oAuth: Boolean = false): Result<String> {
        if (findPossibleProfile(username) != null) {
            println("Username taken")
            return Result.failure(ProfileException(ProfileResult.USERNAME_TAKEN))
        }

        if (email != null) {
            if (findPossibleProfile(email) != null) {
                println("Email taken")
                return Result.failure(ProfileException(ProfileResult.EMAIL_TAKEN))
            }

            if (!oAuth && (!email.contains('@') || !email.contains('.'))) {
                println("Invalid email")
                return Result.failure(ProfileException(ProfileResult.INVALID_EMAIL))
            }
        }

        val salt = if (!oAuth) {
            generateSalt()
        } else {
            null
        }

        val saltedPassword = if (hashedPassword != null && salt != null) {
            saltPassword(hashedPassword, salt)
        } else {
            null
        }

        profileRepository.save(Profile(email, username, saltedPassword, salt, oAuth))
        val token = generateToken(Profile(email, username, saltedPassword, salt, oAuth))

        return Result.success(token)
    }

    fun updateProfile(profile: Profile): Profile {
        return profileRepository.save(profile)
    }

    fun deleteProfile(profile: Profile) {
        profileRepository.delete(profile)
    }

    fun generateToken(profile: Profile): String {
        return JWT.create()
            .withClaim("username", profile.username)
            .withClaim("id", profile.identifier)
            .sign(Algorithm.HMAC256(key))
    }

    fun validateToken(token: String): Boolean {
        return try {
            JWT.require(Algorithm.HMAC256(key)).build().verify(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun decodeToken(token: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token)
    }

    fun findPossibleProfile(identification: String): Profile? {
        return profileRepository.findByUsername(identification) ?: profileRepository.findByEmail(identification)
    }

    fun generateSalt(): String {
        return KeyGenerators.secureRandom(16).generateKey().decodeToString()
    }

    fun saltPassword(password: String, salt: String): ByteArray {
        return DigestUtils.sha256(password + salt)
    }
}