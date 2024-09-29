package com.example.opsc7312_wickedtech.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7312_wickedtech.Models.Workout
import com.example.opsc7312_wickedtech.R

class WorkoutAdapter(
        private val workouts: List<Workout>,
        private val onEditClicked: (Workout) -> Unit
    ) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.textViewExerciseName)
        val exerciseDetails: TextView = itemView.findViewById(R.id.textViewExerciseDetails)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.exerciseName.text = workout.name
        holder.exerciseDetails.text = workout.details

        // Set up the edit button click listener
        holder.editButton.setOnClickListener {
            onEditClicked(workout) // Trigger edit action
        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }
}