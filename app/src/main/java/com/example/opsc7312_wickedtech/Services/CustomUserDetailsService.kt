package com.example.opsc7312_wickedtech.Services

import com.example.opsc7312_wickedtech.Interface.UserRepository
//import com.example.opsc7312_wickedtech.Models.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return User(
            user.username,
            user.password,
            emptyList()  // You can add authorities here if needed
        )
    }
}