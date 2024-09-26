package com.example.opsc7312_wickedtech.Services

import com.example.opsc7312_wickedtech.Models.User
import com.example.opsc7312_wickedtech.Interface.UserRepository
//import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
    ){
    fun registerUser(user: User): User {
        if(userRepository.findByUsername(user.username) != null){
            throw IllegalArgumentException("Username already exists")
        }
        if(userRepository.findByEmail(user.email) != null){
            throw java.lang.IllegalArgumentException("Email already exists")
        }
        val encodedPassword = passwordEncoder.encode(user.password)
        return userRepository.save(user.copy(password = encodedPassword))
    }
}