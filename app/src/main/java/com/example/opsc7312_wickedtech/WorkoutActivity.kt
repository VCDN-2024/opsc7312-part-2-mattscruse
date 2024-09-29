package com.example.opsc7312_wickedtech

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.opsc7312_wickedtech.Activities.LoginActivity

import com.example.opsc7312_wickedtech.Models.Exercise
import com.example.opsc7312_wickedtech.databinding.ActivityWorkoutBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WorkoutActivity :  AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout
        val user = auth.currentUser
        // Check if user is null
        if (user == null) {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java)) // Assuming you have a LoginActivity
            finish()
            return
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, //, binding.toolbar
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        // Set up the click listener for the SAVE button
        binding.button4.setOnClickListener {
            saveExercises()
        }
        // Call fetchExercises to populate existing exercises if any
        fetchExercises()

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
                Toast.makeText(this,"Questionaire Clicked",Toast.LENGTH_SHORT).show()
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

    private fun saveExercises() {
        val exercises = listOf(
            Exercise(name = binding.exercise1EditText.text.toString()),
            Exercise(name = binding.exercise2EditText.text.toString()),
            Exercise(name = binding.exercise3EditText.text.toString()),
            Exercise(name = binding.exercise4EditText.text.toString()),
            Exercise(name = binding.exercise5EditText.text.toString())
        )

        // Save to Firebase
        saveToFirebase(exercises)
    }

    private fun saveToFirebase(exercises: List<Exercise>) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        // Assuming you want to store the exercises in a collection named "user_exercises"
        db.collection("user_exercises").document(userId)
            .set(mapOf("exercises" to exercises))
            .addOnSuccessListener {
                Toast.makeText(this, "Exercises saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving exercises: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    private fun fetchExercises() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("user_exercises").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val exercises = document.get("exercises") as List<Map<String, String>>
                    val exerciseList = exercises.map { Exercise(name = it["name"] ?: "") }
                    // Handle the list of exercises here
                    // e.g., populate the EditText fields with existing data
                    if (exerciseList.isNotEmpty()) {
                        binding.exercise1EditText.setText(exerciseList.getOrNull(0)?.name)
                        binding.exercise2EditText.setText(exerciseList.getOrNull(1)?.name)
                        binding.exercise3EditText.setText(exerciseList.getOrNull(2)?.name)
                        binding.exercise4EditText.setText(exerciseList.getOrNull(3)?.name)
                        binding.exercise5EditText.setText(exerciseList.getOrNull(4)?.name)
                    }
                } else {
                    Toast.makeText(this, "No exercises found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching exercises: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}