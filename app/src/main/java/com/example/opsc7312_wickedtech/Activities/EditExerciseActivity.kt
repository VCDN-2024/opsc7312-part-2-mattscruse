package com.example.opsc7312_wickedtech.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312_wickedtech.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditExerciseActivity  : AppCompatActivity() {
    private lateinit var exerciseNameEditText: EditText
    private lateinit var saveButton: Button
    private var exerciseName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise)

        exerciseNameEditText = findViewById(R.id.exercise_name_edit_text)
        saveButton = findViewById(R.id.save_button)

        exerciseName = intent.getStringExtra("exercise_name")
        exerciseNameEditText.setText(exerciseName)

        saveButton.setOnClickListener {
            updateExercise()
        }
    }

    private fun updateExercise() {
        val updatedName = exerciseNameEditText.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        db.collection("user_exercises").document(userId)
            .update("exercises.${exerciseName}", updatedName) // Update exercise in Firestore
            .addOnSuccessListener {
                Toast.makeText(this, "Exercise updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error updating exercise: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}