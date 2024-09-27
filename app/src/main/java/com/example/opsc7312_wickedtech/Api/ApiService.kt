package com.example.opsc7312_wickedtech.Api

import com.example.opsc7312_wickedtech.Models.LoginModel
import com.example.opsc7312_wickedtech.Models.LoginResponse
import com.example.opsc7312_wickedtech.Models.RegisterModel
import com.example.opsc7312_wickedtech.Models.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface ApiService {
    @POST("api/auth/login")
    fun loginUser(@Body loginModel: LoginModel): Call<LoginResponse>

    @POST("api/auth/register")
    fun registerUser(@Body registerModel: RegisterModel): Call<RegisterResponse>
}