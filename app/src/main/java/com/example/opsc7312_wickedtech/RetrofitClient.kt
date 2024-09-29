package com.example.opsc7312_wickedtech

import com.example.opsc7312_wickedtech.Api.ApiService
import com.example.opsc7312_wickedtech.Interface.FirestoreApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://firestore.googleapis.com/v1/"

    val instance: FirestoreApi by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Set the client
            .build()

        retrofit.create(FirestoreApi::class.java)
    }
}

//https://firestore.googleapis.com/v1/projects/opsc7312-f1b2b/databases/(default)/documents/usersettings/n5mXy6mbHFl5szHWAMPq