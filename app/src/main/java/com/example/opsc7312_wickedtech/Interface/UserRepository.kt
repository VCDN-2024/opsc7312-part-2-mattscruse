package com.example.opsc7312_wickedtech.Interface

import com.example.opsc7312_wickedtech.Models.User

interface UserRepository : JpaRepository<User, Long>{
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

}
