package com.example.opsc7312_wickedtech.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7312_wickedtech.Models.Workout
import com.example.opsc7312_wickedtech.R

class WorkoutAdapter(private val workouts: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.textViewExerciseName)
        val exerciseDetails: TextView = itemView.findViewById(R.id.textViewExerciseDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.exerciseName.text = workout.name
        holder.exerciseDetails.text = workout.details
    }

    override fun getItemCount(): Int {
        return workouts.size
    }
}