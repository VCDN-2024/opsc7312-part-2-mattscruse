package com.example.opsc7312_wickedtech

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.opsc7312_wickedtech.Models.Fields
import com.example.opsc7312_wickedtech.Models.FirestoreDoubleValue
import com.example.opsc7312_wickedtech.Models.FirestoreIntegerValue
import com.example.opsc7312_wickedtech.Models.FirestoreStringValue
import com.example.opsc7312_wickedtech.Models.UserSettings
import com.example.opsc7312_wickedtech.databinding.ActivitySettingsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {


    private lateinit var binding: ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout
        val user = auth.currentUser

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

//        setSupportActionBar(binding.toolbar)

                fetchUserSettings()


        // Save settings when the user clicks the save button
        findViewById<Button>(R.id.button9).setOnClickListener {
            saveUserSettings()
        }

            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, //, binding.toolbar
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            binding.navView.setNavigationItemSelectedListener(this)

        }
    private fun saveUserSettings() {
        try{
        // Get input values
        val genderInput = findViewById<TextInputEditText>(R.id.genderInput).text.toString()
        val ageInput = findViewById<TextInputEditText>(R.id.ageInput).text.toString().toIntOrNull()
        val weightInput = findViewById<TextInputEditText>(R.id.weightInput).text.toString().toFloatOrNull() ?: 0.0f
        val heightInput = findViewById<TextInputEditText>(R.id.heightInput).text.toString().toFloatOrNull() ?: 0.0f

        // Check if age is valid
        if (ageInput == null) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a Firestore-compatible object
            val newSettings = UserSettings(
                fields = Fields(
                    gender = FirestoreStringValue(genderInput),
                    age = FirestoreIntegerValue(ageInput),
                    height = FirestoreDoubleValue(heightInput),
                    weight = FirestoreDoubleValue(weightInput)
                )
            )
            // Make API call to save settings
        RetrofitClient.instance.updateUserSettings(
            "opsc7312-f1b2b",
            "n5mXy6mbHFl5szHWAMPq",
            newSettings
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SettingsActivity, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.d(TAG, "Failed to save settings: $errorBody")
                    Toast.makeText(this@SettingsActivity, "Failed to save settings: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Error in Firestore API call: ${t.message}")
                Toast.makeText(this@SettingsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    } catch (e: Exception) {
        Log.e(TAG, "Error occurred while saving settings: ${e.message}")
        Toast.makeText(this@SettingsActivity, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
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

                R.id.nav_questionaire -> {
                    Toast.makeText(this, "Questionaire Clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_workout -> {
                    Toast.makeText(this, "Workout Clicked", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, WorkoutActivity::class.java))
                    finish()
                }
                // Add more cases for other menu items
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
    private fun fetchUserSettings() {
        RetrofitClient.instance.getUserSettings("opsc7312-f1b2b", "usersettings", "n5mXy6mbHFl5szHWAMPq")
            .enqueue(object : Callback<UserSettings> {
                override fun onResponse(call: Call<UserSettings>, response: Response<UserSettings>) {
                    if (response.isSuccessful) {
                        val userSettings = response.body()
                        userSettings?.let {
                            populateUIWithUserSettings(it)
                        } ?: run {
                            Toast.makeText(this@SettingsActivity, "No settings found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SettingsActivity, "Failed to fetch settings", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserSettings>, t: Throwable) {
                    Toast.makeText(this@SettingsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun populateUIWithUserSettings(settings: UserSettings) {
        // Populate UI with user settings (e.g., gender, age, etc.)
        binding.genderInput.setText(settings.fields.gender.stringValue)
        binding.ageInput.setText(settings.fields.age.integerValue.toString())
        binding.weightInput.setText(settings.fields.weight.doubleValue.toString())
        binding.heightInput.setText(settings.fields.height.doubleValue.toString())
    }
}