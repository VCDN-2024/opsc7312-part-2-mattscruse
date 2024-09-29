package com.example.opsc7312_wickedtech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.opsc7312_wickedtech.Models.UserSettings
import com.example.opsc7312_wickedtech.databinding.ActivityBmiBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BMIActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityBmiBinding
    private lateinit var bmiResult: TextView
    private lateinit var refreshButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout
        val user = auth.currentUser

        bmiResult = binding.bmiResult
        refreshButton = binding.refreshButton
        fetchUserSettings()

        // Set up the NavigationView
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        refreshButton.setOnClickListener {
            fetchUserSettings() // Refresh BMI when button is clicked
        }

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_bmi -> {
                Toast.makeText(this,"BMI Clicked",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BMIActivity::class.java))
                finish()
            }

            R.id.nav_workout -> {
                Toast.makeText(this, "Workout Clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutActivity::class.java))
                finish()
            }
            R.id.nav_workout_list -> {
                Toast.makeText(this, "Workout Clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutListActivity::class.java))
                finish()
            }

            // Add more cases for other menu items
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun fetchUserSettings() {
        val documentId = "n5mXy6mbHFl5szHWAMPq"
        RetrofitClient.instance.getUserSettings("opsc7312-f1b2b", "usersettings", documentId)
            .enqueue(object : Callback<UserSettings> {
                override fun onResponse(call: Call<UserSettings>, response: Response<UserSettings>) {
                    if (response.isSuccessful) {
                        val userSettings = response.body()
                        userSettings?.let { settings ->
                            // Access height and weight through the fields
                            val height = settings.fields.height.doubleValue
                            val weight = settings.fields.weight.doubleValue

                            // Log the retrieved values
                            Log.d("BMIActivity", "Height: $height, Weight: $weight")

                            // Check for valid height and weight
                            if (height > 0 && weight > 0) {
                                // Calculate and display BMI
                                val bmi = calculateBMI(height, weight)
                                bmiResult.text = "Your BMI is: ${String.format("%.2f", bmi)}"
                                Log.d("BMIActivity", "BMI set to: ${bmiResult.text}")
                            } else {
                                Log.e("BMIActivity", "Invalid height or weight")
                                bmiResult.text = "Invalid height or weight"
                            }
                        } ?: run {
                            Toast.makeText(this@BMIActivity, "No settings found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("BMIActivity", "Failed to fetch settings: ${response.code()} - ${response.message()}")
                        Toast.makeText(this@BMIActivity, "Failed to fetch settings", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserSettings>, t: Throwable) {
                    Log.e("BMIActivity", "Error in fetching user settings: ${t.message}")
                    Toast.makeText(this@BMIActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun calculateBMI(height: Float, weight: Float): Number {
        val heightInMeters = height / 100 // Convert cm to meters
        return if (heightInMeters > 0) {
            weight / (heightInMeters * heightInMeters)
        } else {
            0.0
        }
    }

}