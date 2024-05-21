package dev.loudbook.pastebook.controllers.profile

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dev.loudbook.pastebook.data.RedisService
import dev.loudbook.pastebook.data.user.profile.ProfileDTO
import dev.loudbook.pastebook.data.user.profile.ProfileService
import jakarta.servlet.http.HttpServletRequest
import kong.unirest.Unirest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController {
    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var redisService: RedisService

    @Value("\${discord.client-id}")
    private lateinit var discordClientId: String

    @Value("\${discord.client-secret}")
    private lateinit var discordClientSecret: String

    @PostMapping("/signup")
    fun signup(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().fromJson(request.reader.readText(), JsonObject::class.java)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val username = json.get("username")?.asString ?: return ResponseEntity.badRequest().body("Missing username")
        val email = json.get("email")?.asString ?: return ResponseEntity.badRequest().body("Missing email")
        val password = json.get("password")?.asString ?: return ResponseEntity.badRequest().body("Missing password")
        val sixDigitCode = json.get("code")?.asString ?: return ResponseEntity.badRequest().body("Missing verification code")

        val result = profileService.processAccountCreation(username, email, password, sixDigitCode)
        return if (result.isSuccess) {
            ResponseEntity.ok().body(ProfileDTO(result.getOrThrow(), email, username).toJson())
        } else {
            ResponseEntity.badRequest().body(result.exceptionOrNull()?.message)
        }
    }

    @PostMapping("/login")
    fun login(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().fromJson(request.reader.readText(), JsonObject::class.java)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val identification = json.get("identification")?.asString ?: return ResponseEntity.badRequest().body("Missing identification")
        val password = json.get("password")?.asString ?: return ResponseEntity.badRequest().body("Missing password")

        val result = profileService.processLogin(identification, password)
        return if (result.isSuccess) {
            val username = profileService.findPossibleProfile(identification)?.username ?: return ResponseEntity.badRequest().body("Invalid identification")

            ResponseEntity.ok().body(ProfileDTO(result.getOrThrow(), identification, username).toJson())
        } else {
            println(result.exceptionOrNull()?.message)
            ResponseEntity.badRequest().body(result.exceptionOrNull()?.message)
        }
    }

    @GetMapping("/validation/username")
    fun validateUsername(request: HttpServletRequest): ResponseEntity<String> {
        val profile = profileService.findPossibleProfile(request.getHeader("username") ?: return ResponseEntity.badRequest().body(null))

        return if (profile == null) {
            ResponseEntity.status(200).body(null)
        } else {
            ResponseEntity.status(409).body(null)
        }
    }

    @GetMapping("/validation/email")
    fun validateEmail(request: HttpServletRequest): ResponseEntity<String> {
        val profile = profileService.findPossibleProfile(request.getHeader("email") ?: return ResponseEntity.badRequest().body(null))

        return if (profile == null) {
            ResponseEntity.status(200).body(null)
        } else {
            ResponseEntity.status(409).body(null)
        }
    }

    @PostMapping("/login/requestEmail")
    fun requestEmail(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().fromJson(request.reader.readText(), JsonObject::class.java)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val email = json.get("email")?.asString ?: return ResponseEntity.badRequest().body("Missing email")
        val username = json.get("username")?.asString ?: return ResponseEntity.badRequest().body("Missing username")

        if (profileService.findPossibleProfile(email) != null) {
            return ResponseEntity.badRequest().body("Email taken")
        }

        if (profileService.findPossibleProfile(username) != null) {
            return ResponseEntity.badRequest().body("Username taken")
        }

        profileService.requestEmailConfirmation(email)
        return ResponseEntity.ok().body(null)
    }

    @GetMapping("/login/discord")
    fun loginDiscord(@RequestParam code: String, request: HttpServletRequest): ResponseEntity<String> {
        val response = Unirest.post("https://discord.com/api/oauth2/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .basicAuth(discordClientId, discordClientSecret)
            .field("grant_type", "authorization_code")
            .field("code", code)
            .field("redirect_uri", "http://localhost:5173/login/discord")
            .field("client_id", discordClientId)
            .field("client_secret", discordClientSecret)
            .field("scope", "identify")
            .asString()

        val accessToken = try {
            JsonParser.parseString(response.body).asJsonObject.get("access_token").asString
        } catch (e: NullPointerException) {
            return ResponseEntity.badRequest().body("Invalid Discord response")
        }

        val dataRequestHeaders = HttpHeaders()
        dataRequestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        dataRequestHeaders.set("Authorization", "Bearer $accessToken")

        val dataResponse = Unirest.get("https://discord.com/api/users/@me")
            .header("Authorization ", "Bearer $accessToken")
            .asString()

        val json = try {
            JsonParser.parseString(dataResponse.body).asJsonObject
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid Discord response")
        }

        val id = json.get("id").asString
            ?: return ResponseEntity.badRequest().body("Invalid Discord response")
        val username = json.get("username").asString
            ?: return ResponseEntity.badRequest().body("Invalid Discord response")

        if (profileService.findPossibleProfile(id) != null || profileService.findPossibleProfile(username) != null) {
            val processedResult = profileService.processLogin(id)

            return ResponseEntity.ok().body(ProfileDTO(processedResult.getOrThrow(), id, username).toJson())
        }

        val result = profileService.processAccountCreation(username, id, null, null, true)

        return if (result.isSuccess) {
            ResponseEntity.ok().body(ProfileDTO(result.getOrThrow(), id, username).toJson())
        } else {
            ResponseEntity.badRequest().body(result.exceptionOrNull()?.message)
        }
    }

    @PatchMapping("/update")
    fun updateProfile(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().fromJson(request.reader.readText(), JsonObject::class.java)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val token = request.getHeader("Authorization") ?: return ResponseEntity.badRequest().body("Missing authorization header")

        val id = redisService.getToken(token) ?: return ResponseEntity.badRequest().body("Invalid authorization header")
        var currentUser = profileService.findPossibleProfile(id) ?: return ResponseEntity.badRequest().body("Invalid authorization header")

        if (json.has("username")) {
            val username = json.get("username").asString
            if (profileService.findPossibleProfile(username) != null) {
                return ResponseEntity.badRequest().body("Username taken")
            }

            currentUser = currentUser.copy(username = username)
        }

        if (json.has("email") && !currentUser.oAuth) {
            val email = json.get("email").asString
            if (profileService.findPossibleProfile(email) != null) {
                return ResponseEntity.badRequest().body("Email taken")
            }

            currentUser = currentUser.copy(identifier = email)
        }

        if (json.has("password") && !currentUser.oAuth) {
            val password = json.get("password").asString
            val newSalt = profileService.generateSalt()
            currentUser = currentUser.copy(password = profileService.saltPassword(password, newSalt), salt = newSalt)
        }

        redisService.deleteToken(token)

        return ResponseEntity.ok().body(ProfileDTO(profileService.updateProfile(currentUser), currentUser.identifier, currentUser.username).toJson())
    }

    @DeleteMapping("/delete")
    fun deleteProfile(request: HttpServletRequest): ResponseEntity<String> {
        val token = request.getHeader("Authorization") ?: return ResponseEntity.badRequest().body("Missing authorization header")

        val id = redisService.getToken(token) ?: return ResponseEntity.badRequest().body("Invalid authorization header")
        val currentUser = profileService.findPossibleProfile(id) ?: return ResponseEntity.badRequest().body("Invalid authorization header")

        profileService.deleteProfile(currentUser)
        redisService.deleteToken(token)
        return ResponseEntity.ok().body(null)
    }
}