package com.example.opsc7312_wickedtech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.opsc7312_wickedtech.Adapters.ExerciseAdapter
import com.example.opsc7312_wickedtech.Models.Exercise
import com.example.opsc7312_wickedtech.databinding.ActivityWorkoutListBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class WorkoutListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var exerciseList: MutableList<Exercise>
    private lateinit var binding: ActivityWorkoutListBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views using binding
        val recyclerView = binding.recyclerView // Use the view binding for RecyclerView
        drawerLayout = binding.drawerLayout
        recyclerView.layoutManager = LinearLayoutManager(this)

        exerciseList = mutableListOf()
        exerciseAdapter = ExerciseAdapter(exerciseList) { exercise ->
            editExercise(exercise) // Handle edit click
        }

        recyclerView.adapter = exerciseAdapter

        // Fetch exercises from Firestore
        fetchExercises()

        // Setup the NavigationView
        val navView: NavigationView = binding.navView // Access NavigationView through binding
        navView.setNavigationItemSelectedListener(this)
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

    private fun fetchExercises() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("user_exercises").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("WorkoutListActivity", "Document retrieved: ${document.data}") // Log the entire document

                    // Attempt to get the exercises field
                    val exercises = document.get("exercises") as? List<Map<String, Any>>

                    // Debugging: Log what was retrieved
                    Log.d("WorkoutListActivity", "Exercises retrieved: $exercises")

                    if (exercises != null) {
                        exerciseList.clear() // Clear the list to prevent duplicates

                        if (exercises.isNotEmpty()) {
                            for (exerciseData in exercises) {
                                // Safely extract values
                                val name = exerciseData["name"] as? String ?: "Unknown"
                                val duration = (exerciseData["duration"] as? Long)?.toInt() ?: 0
                                val sets = (exerciseData["sets"] as? Long)?.toInt() ?: 0
                                val reps = (exerciseData["reps"] as? Long)?.toInt() ?: 0
                                val documentId = exerciseData["documentId"] as? String ?: ""

                                // Add the Exercise object to the list
                                exerciseList.add(Exercise(name, duration, sets, reps, documentId))
                            }
                            exerciseAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(this, "No exercises found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Exercises field is missing or incorrectly formatted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No exercises found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching exercises: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun editExercise(exercise: Exercise) {
        // Start an edit activity, passing the selected exercise's details
        val intent = Intent(this, EditExerciseActivity::class.java).apply {
            putExtra("exercise_name", exercise.name)
            putExtra("exercise_duration", exercise.duration)
            putExtra("exercise_sets", exercise.sets)
            putExtra("exercise_reps", exercise.reps)
            putExtra("document_id", exercise.documentId) // Pass the document ID for updates
        }
        startActivity(intent)
    }
}
