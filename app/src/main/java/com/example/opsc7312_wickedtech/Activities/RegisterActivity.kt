package com.example.opsc7312_wickedtech.Activities

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312_wickedtech.Models.RegisterModel
import com.example.opsc7312_wickedtech.Models.RegisterResponse
import com.example.opsc7312_wickedtech.R
import com.example.opsc7312_wickedtech.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private fun registerUser() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val registerModel = RegisterModel(username, email, password)

        // Make the API call
        RetrofitClient.apiService.registerUser(registerModel).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    // Optionally, you can navigate to the login activity after successful registration
                    // startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    // finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}