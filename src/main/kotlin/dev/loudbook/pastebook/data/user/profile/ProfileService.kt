package dev.loudbook.pastebook.data.user.profile

import dev.loudbook.pastebook.data.RedisService
import dev.loudbook.pastebook.mongo.ProfileRepository
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.keygen.KeyGenerators
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class ProfileService {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var redisService: RedisService

    fun processLogin(identification: String, password: String? = null): Result<String> {
        val profile = findPossibleProfile(identification)
            ?: return Result.failure(ProfileException(ProfileResult.USERNAME_NOT_FOUND))

        if (!profile.oAuth && (password == null || profile.salt == null)) {
            return Result.failure(ProfileException(ProfileResult.INCORRECT_PASSWORD))
        }

        if (password != null && !profile.oAuth && profile.salt != null) {
            val attemptedPasswordWithSalt = saltPassword(password, profile.salt)

            if (!profile.password.contentEquals(attemptedPasswordWithSalt)) {
                return Result.failure(ProfileException(ProfileResult.INCORRECT_PASSWORD))
            }
        }

        return Result.success(generateToken(profile.identifier))
    }

    fun processAccountCreation(username: String, email: String, hashedPassword: String?, oAuth: Boolean = false): Result<String> {
        if (findPossibleProfile(username) != null) {
            println("Username taken")
            return Result.failure(ProfileException(ProfileResult.USERNAME_TAKEN))
        }

        if (findPossibleProfile(email) != null) {
            println("Email taken")
            return Result.failure(ProfileException(ProfileResult.EMAIL_TAKEN))
        }

        if (!oAuth && (!email.contains('@') || !email.contains('.'))) {
            println("Invalid email")
            return Result.failure(ProfileException(ProfileResult.INVALID_EMAIL))
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
        val token = generateToken(email)

        return Result.success(token)
    }

    fun updateProfile(profile: Profile): String {
        profileRepository.save(profile)

        return generateToken(profile.identifier)
    }

    fun deleteProfile(profile: Profile) {
        profileRepository.delete(profile)
    }

    fun generateToken(id: String): String {
        val random = SecureRandom()
        val bytes = ByteArray(20)
        random.nextBytes(bytes)

        redisService.cacheToken(bytes.toString(), id)

        return bytes.toString()
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