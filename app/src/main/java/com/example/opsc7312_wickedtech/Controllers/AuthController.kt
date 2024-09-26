package com.example.opsc7312_wickedtech.Controllers

import com.example.opsc7312_wickedtech.Models.User

class AuthController {
    @RestController
    @RequestMapping("api/auth")
    class  AuthController(private val userService: UserService){


        @PostMapping("/register")
        fun register(@RequestBody user: User): ResponseEntity<Any>{
            return try{
                val registeredUser = userService.registerUser(user)
                ResponseEntity.ok(registeredUser)
            }catch (e: Exception){
                ResponseEntity.badRequest().body(mapOf("error" to e.message))
            }
        }

        @PostMapping("/login")
        fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any>{
            return try {
                val token = userService.loginUser(loginRequest.username, loginRequest.password)
                ResponseEntity.ok(mapOf("token" to token))
            }catch (e: Exception){
                ResponseEntity.badRequest().body(mapOf("error" to e.message))
            }
        }
    }
}