package com.example.opsc7312_wickedtech.Models

import android.telecom.Call.Details

data class Exercise (
    val name: String = "",
    val details: String = "",
    val duration: Int = 0,
    val sets: Int = 0,
    val reps: String = "",
    val documentId: String = ""
)