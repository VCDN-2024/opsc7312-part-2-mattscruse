package com.example.opsc7312_wickedtech

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.opsc7312_wickedtech.Activities.EditExerciseActivity
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
        binding.drawerLayout // Access drawerLayout through binding
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
                    val exercises = document.get("exercises") as List<Map<String, String>>
                    exerciseList.clear()
                    exerciseList.addAll(exercises.map { Exercise(it["name"] ?: "") })
                    exerciseAdapter.notifyDataSetChanged()
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
        val intent = Intent(this, EditExerciseActivity::class.java)
        intent.putExtra("exercise_name", exercise.name) // Pass the exercise name to the edit activity
        startActivity(intent)
    }
}
