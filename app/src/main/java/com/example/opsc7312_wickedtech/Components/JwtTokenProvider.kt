package com.example.opsc7312_wickedtech.Components

import com.google.android.datatransport.runtime.dagger.Component
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication

import java.util.*

@Component
class JwtTokenProvider {

    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwtExpirationInMs}")
    private var jwtExpirationInMs: Int = 0

   private lateinit var key: SecretKey

   fun initializeKey(){
       key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
   }

    fun generateToken(authentication: org.springframework.security.core.Authentication): String{
        val userPrincipal = authentication.principal as org.springframework.security.core.userdetails.User
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact()
    }
    fun getUsernameFromJWT(token: String): String {
        if (!::key.isInitialized) {
            initializeKey() // Ensures key is initialized
        }
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject

    }
    fun validateToken(authToken: String): Boolean {
        if (!::key.isInitialized) {
            initializeKey() // Ensures key is initialized
        }
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
            true
        } catch (ex: Exception) {
            // Log the exception
            false
        }
    }
}