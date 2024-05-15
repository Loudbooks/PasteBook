package dev.loudbook.pastebook.controllers.profile

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.loudbook.pastebook.data.user.profile.ProfileService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api/profile")
class ProfileController {
    @Autowired
    lateinit var profileService: ProfileService

    @PostMapping("/signup")
    fun signup(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().toJsonTree(request.reader.readText()) as JsonObject
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val username = json.get("username")?.asString ?: return ResponseEntity.badRequest().body("Missing username")
        val email = json.get("email")?.asString ?: return ResponseEntity.badRequest().body("Missing email")
        val password = json.get("password")?.asString ?: return ResponseEntity.badRequest().body("Missing password")

        val result = profileService.processAccountCreation(username, email, password)
        return if (result.isSuccess) {
            ResponseEntity.ok().body(result.getOrNull())
        } else {
            ResponseEntity.badRequest().body(result.exceptionOrNull()?.message)
        }
    }

    @PostMapping("/login")
    fun login(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().toJsonTree(request.reader.readText()) as JsonObject
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val identification = json.get("identification")?.asString ?: return ResponseEntity.badRequest().body("Missing identification")
        val password = json.get("password")?.asString ?: return ResponseEntity.badRequest().body("Missing password")

        val result = profileService.processLogin(identification, password)
        return if (result.isSuccess) {
            ResponseEntity.ok().body(result.getOrNull())
        } else {
            ResponseEntity.badRequest().body(result.exceptionOrNull()?.message)
        }
    }

    @PostMapping("/login/discord")
    fun loginDiscord(@RequestParam code: String, request: HttpServletRequest): ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val requestJson = JsonObject()
        requestJson.addProperty("grant_type", "authorization_code")
        requestJson.addProperty("code", code)
        requestJson.addProperty("redirect_uri", "https://pastebook.dev/api/profile/login/discord")

        val response = Gson().toJsonTree(restTemplate.exchange(
            UriComponentsBuilder.fromHttpUrl("https://discord.com/api/oauth2/token")
                .toUriString(),
            HttpMethod.POST,
            HttpEntity(requestJson.toString(), headers),
            String::class.java))

        val accessToken = response.asJsonObject.get("access_token").asString
        val dataRequestHeaders = HttpHeaders()
        dataRequestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        dataRequestHeaders.set("Authorization", "Bearer $accessToken")

        val dataRequestEntity: HttpEntity<Any> = HttpEntity(dataRequestHeaders)
        val dataResponse = Gson().toJsonTree(restTemplate.exchange(
            UriComponentsBuilder.fromHttpUrl("https://discord.com/api/users/@me")
                .toUriString(),
            HttpMethod.GET,
            dataRequestEntity,
            String::class.java))

        val id = dataResponse.asJsonObject.get("id").asString
            ?: return ResponseEntity.badRequest().body("Invalid Discord response")
        val username = dataResponse.asJsonObject.get("username").asString
            ?: return ResponseEntity.badRequest().body("Invalid Discord response")

        if (profileService.findPossibleProfile(id) != null) {
            return ResponseEntity.ok().body(profileService.processLogin(id).getOrNull())

        }

        val result = profileService.processAccountCreation(username, id, null, true)

        return if (result.isSuccess) {
            ResponseEntity.ok().body(result.getOrNull())
        } else {
            ResponseEntity.badRequest().body(result.exceptionOrNull()?.message)
        }
    }

    @PatchMapping("/update")
    fun updateProfile(request: HttpServletRequest): ResponseEntity<String> {
        val json = try {
            Gson().toJsonTree(request.reader.readText()) as JsonObject
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid JSON")
        }

        val jwt = request.getHeader("Authorization") ?: return ResponseEntity.badRequest().body("Missing JWT")
        if (!profileService.validateToken(jwt)) {
            return ResponseEntity.badRequest().body("Invalid JWT")
        }

        val id = profileService.decodeToken(jwt).getClaim("id").asString()
        var currentUser = profileService.findPossibleProfile(id) ?: return ResponseEntity.badRequest().body("Invalid JWT")

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

        return ResponseEntity.ok().body(profileService.updateProfile(currentUser).toString())
    }

    @DeleteMapping("/delete")
    fun deleteProfile(request: HttpServletRequest): ResponseEntity<String> {
        val jwt = request.getHeader("Authorization") ?: return ResponseEntity.badRequest().body("Missing JWT")
        if (!profileService.validateToken(jwt)) {
            return ResponseEntity.badRequest().body("Invalid JWT")
        }

        val id = profileService.decodeToken(jwt).getClaim("id").asString()
        val currentUser = profileService.findPossibleProfile(id) ?: return ResponseEntity.badRequest().body("Invalid JWT")

        profileService.deleteProfile(currentUser)
        return ResponseEntity.ok().body("Deleted")
    }
}