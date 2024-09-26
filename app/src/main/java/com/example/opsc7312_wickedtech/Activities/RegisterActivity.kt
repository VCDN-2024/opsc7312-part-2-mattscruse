package com.example.opsc7312_wickedtech.Activities

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312_wickedtech.R

class RegisterActivity: AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var  etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        // Set click listener for register button
        btnRegister.setOnClickListener {
            registerUser()
        }
}
    private fun registerUser(){
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
        // TODO: Implement actual registration logic here
        // This is where you would typically make an API call to your backend server
        // For now, we'll just show a success message
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

        // TODO: After successful registration, you might want to automatically log the user in
        // and navigate to the main activity of your app
        // For now, we'll just finish this activity, returning to the previous screen
        finish()
    }
}