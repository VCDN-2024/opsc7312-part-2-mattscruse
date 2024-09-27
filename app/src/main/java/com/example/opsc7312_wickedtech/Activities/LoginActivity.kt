package com.example.opsc7312_wickedtech.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312_wickedtech.Models.LoginModel
import com.example.opsc7312_wickedtech.Models.LoginResponse
import com.example.opsc7312_wickedtech.R
import com.example.opsc7312_wickedtech.RetrofitClient
import retrofit2.Call

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegisterLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initialize Variables
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegisterLink = findViewById(R.id.tvRegisterLink)

        btnLogin.setOnClickListener {
            loginUser()
        }

        tvRegisterLink.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
}

    private fun loginUser() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val loginModel = LoginModel(username, password)

        // Make the API call
        RetrofitClient.apiService.loginUser(loginModel)
            .enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: retrofit2.Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        // Save token and proceed to next activity
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT)
                            .show()
                        // Start next activity (e.g., MainActivity)
                        // Save token in SharedPreferences or wherever you prefer
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Failed: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}