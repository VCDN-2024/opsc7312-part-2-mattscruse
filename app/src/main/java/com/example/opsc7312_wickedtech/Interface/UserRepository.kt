package com.example.opsc7312_wickedtech.Interface

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveUserData(userId: String, name: String, email: String) {
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                println("User data saved successfully")
            }
            .addOnFailureListener { e ->
                println("Error saving user data: $e")
            }
    }

    fun getCurrentUser() = auth.currentUser
}