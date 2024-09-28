package com.example.opsc7312_wickedtech.Api

import com.example.opsc7312_wickedtech.Models.LoginResponse
import com.example.opsc7312_wickedtech.Models.RegisterResponse
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

class ApiService {
//    @POST("api/auth/login")
//    fun loginUser(@Body loginModel: LoginModel): Call<LoginResponse>
//
//    @POST("api/auth/register")
//    fun registerUser(@Body registerModel: RegisterModel): Call<RegisterResponse>

    private val functions: FirebaseFunctions = Firebase.functions

    suspend fun callApi(functionName: String, data: Map<String, Any>): Result<String> {
        return try {
            val result = functions
                .getHttpsCallable(functionName)
                .call(data)
                .await()
            Result.success(result.data as String)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}