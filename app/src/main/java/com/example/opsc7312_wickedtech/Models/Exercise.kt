package com.example.opsc7312_wickedtech.Models

import android.telecom.Call.Details

data class Exercise (
    val name: String = "",
    val duration: Int = 0,
    val sets: Int = 0,
    val reps: Int = 0,
    val documentId: String = ""
)