package com.example.opsc7312_wickedtech.Interface


import com.example.opsc7312_wickedtech.Models.UserSettings
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FirestoreApi {


    // Fetch user settings
    @GET("projects/{projectId}/databases/(default)/documents/{collectionId}/{documentId}")
    fun getUserSettings(
        @Path("projectId") projectId: String,
        @Path("collectionId") collectionId: String,
        @Path("documentId") documentId: String
    ): Call<UserSettings>

    // Post new user settings
    @PATCH("projects/{projectId}/databases/(default)/documents/usersettings/{documentId}")
    fun updateUserSettings(
        @Path("projectId") projectId: String,
        @Path("documentId") documentId: String,
        @Body userSettings: UserSettings
    ): Call<Void>
}