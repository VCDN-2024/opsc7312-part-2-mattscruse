package com.example.opsc7312_wickedtech

import android.os.Bundle
import android.util.Log
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
    private lateinit var editTextDuration: EditText // Add field for duration
    private lateinit var editTextSets: EditText // Add field for sets
    private lateinit var editTextReps: EditText // Add field for reps
    private lateinit var buttonSave: Button
     private var documentId = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise)

        editTextExerciseName = findViewById(R.id.editTextExerciseName)
        editTextExerciseDetails = findViewById(R.id.editTextExerciseDetails)
        editTextDuration = findViewById(R.id.editTextDuration) // Initialize the duration field
        editTextSets = findViewById(R.id.editTextSets) // Initialize the sets field
        editTextReps = findViewById(R.id.editTextReps) // Initialize the reps field
        buttonSave = findViewById(R.id.buttonSave)

        // Get data passed from WorkoutListActivity
        val workoutName = intent.getStringExtra("exercise_name") ?: ""
        val workoutDetails = intent.getStringExtra("exercise_details") ?: ""
        val workoutDuration = intent.getIntExtra("exercise_duration", 0)
        val workoutSets = intent.getIntExtra("exercise_sets", 0)
        val workoutReps = intent.getIntExtra("exercise_reps", 0)

        documentId =
            (intent.getStringExtra("document_id") ?: "") // Get exercise ID from intent

        // Logging to troubleshoot
        Log.d("EditExerciseActivity", "Received Data: Name: $workoutName, Details: $workoutDetails, Duration: $workoutDuration, Sets: $workoutSets, Reps: $workoutReps, ID: $documentId")

        // Populate fields with existing workout data
        editTextExerciseName.setText(workoutName)
        editTextExerciseDetails.setText(workoutDetails)
        editTextDuration.setText(workoutDuration.toString()) // Populate duration
        editTextSets.setText(workoutSets.toString()) // Populate sets
        editTextReps.setText(workoutReps.toString()) // Populate reps

        buttonSave.setOnClickListener {
            updateExercise() // Call updateExercise instead of updateWorkout
        }
    }

    private fun updateExercise() {
        val updatedName = editTextExerciseName.text.toString().trim()
        val updatedDetails = editTextExerciseDetails.text.toString().trim()
        val updatedDuration = editTextDuration.text.toString().trim().toIntOrNull() ?: 0 // Handle duration input
        val updatedSets = editTextSets.text.toString().trim().toIntOrNull() ?: 0 // Handle sets input
        val updatedReps = editTextReps.text.toString().trim().toIntOrNull() ?: 0 // Handle reps input

        if (updatedName.isEmpty() || updatedDetails.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        // Update the exercise in Firestore
        val exerciseUpdates = mapOf(
            "name" to updatedName,
            "details" to updatedDetails,
            "duration" to updatedDuration,
            "sets" to updatedSets,
            "reps" to updatedReps
        )

        // Update the specific exercise using its document ID
        db.collection("user_exercises").document(userId)
        if (documentId.isEmpty()) {
            Toast.makeText(this, "Invalid exercise document ID", Toast.LENGTH_SHORT).show()
            Log.e("EditExerciseActivity", "Exercise document ID is empty")
            return
        } else {
            db.collection("user_exercises").document(userId)
            .update("exercises.$documentId", exerciseUpdates) // Adjust as necessary based on your Firestore structure
                .addOnSuccessListener {
                    Toast.makeText(this, "Exercise updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after saving
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error updating exercise: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }


    }
}
