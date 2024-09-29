package com.example.opsc7312_wickedtech

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312_wickedtech.Models.Exercise // Import the Exercise model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditExerciseActivity : AppCompatActivity() {

    private lateinit var editTextExerciseName: EditText
    private lateinit var editTextExerciseDetails: EditText
    private lateinit var buttonSave: Button
    private lateinit var exerciseId: String // Add a variable for exercise ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise)

        editTextExerciseName = findViewById(R.id.editTextExerciseName)
        editTextExerciseDetails = findViewById(R.id.editTextExerciseDetails)
        buttonSave = findViewById(R.id.buttonSave)

        // Get data passed from WorkoutListActivity
        val workoutName = intent.getStringExtra("exercise_name") ?: ""
        val workoutDetails = intent.getStringExtra("exercise_details") ?: "" // Ensure this matches what you're passing
        val workoutDuration = intent.getIntExtra("exercise_duration", 0) // New field for duration
        val workoutSets = intent.getIntExtra("exercise_sets", 0) // New field for sets
        val workoutReps = intent.getIntExtra("exercise_reps", 0) // New field for reps

        // Populate fields with existing workout data
        editTextExerciseName.setText(workoutName)
        editTextExerciseDetails.setText(workoutDetails)

        buttonSave.setOnClickListener {
            updateExercise() // Call updateExercise instead of updateWorkout
        }
    }

    private fun updateExercise() { // Update method name to match context
        val updatedName = editTextExerciseName.text.toString().trim()
        val updatedDetails = editTextExerciseDetails.text.toString().trim()

        if (updatedName.isEmpty() || updatedDetails.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        // Update the exercise in Firestore
        val exerciseUpdates = mapOf(
            "name" to updatedName,
            "details" to updatedDetails
        )

        // Update the specific exercise using its document ID
        db.collection("user_exercises").document(userId)
            .update("exercises.$exerciseId", exerciseUpdates) // Adjust as necessary based on your Firestore structure
            .addOnSuccessListener {
                Toast.makeText(this, "Exercise updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after saving
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error updating exercise: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
