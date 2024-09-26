package com.example.opsc7312_wickedtech.Services

import com.example.opsc7312_wickedtech.Interface.UserRepository
import com.example.opsc7312_wickedtech.Models.User

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder, private val jwtTokenProvider: JwtTokenProvider) {
    fun registerUser(user: User): User {
        if (userRepository.findByUsername(user.username) != null) {
            throw IllegalArgumentException("Username already exists")
        }
        if (userRepository.findByEmail(user.email) != null) {
            throw IllegalArgumentException("Email already exists")
        }
        val encodedPassword = passwordEncoder.encode(user.password)
        return userRepository.save(user.copy(password = encodedPassword))
    }

    fun loginUser(username: String, password: String): String {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("Invalid username or password")
        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }
        return jwtTokenProvider.createToken(user.username)
    }
}