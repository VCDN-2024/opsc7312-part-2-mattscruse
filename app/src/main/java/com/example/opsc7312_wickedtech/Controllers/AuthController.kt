package com.example.opsc7312_wickedtech.Controllers

import com.example.opsc7312_wickedtech.Services.UserService
import com.example.opsc7312_wickedtech.Components.JwtTokenProvider
import com.example.opsc7312_wickedtech.Models.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider

    ) {

    @PostMapping("/register")
    fun register(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<Any> {
        return try {
            val user = User(
                username = registrationRequest.username,
                email = registrationRequest.email,
                password = registrationRequest.password
            )
            val registeredUser = userService.registerUser(user)
            ResponseEntity.ok(UserResponse(registeredUser.id, registeredUser.username, registeredUser.email))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(ErrorResponse(e.message ?: "Registration failed"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse("An unexpected error occurred"))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            )
            val jwt = jwtTokenProvider.generateToken(authentication)
            ResponseEntity.ok(AuthResponse(jwt))
        } catch (e: AuthenticationException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse("Invalid username or password"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse("An unexpected error occurred"))
        }
    }
}