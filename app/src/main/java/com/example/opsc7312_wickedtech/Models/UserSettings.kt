package com.example.opsc7312_wickedtech.Models

data class UserSettings(
    val fields: Fields
)

data class Fields(
    val age: FirestoreIntegerValue,
    val gender: FirestoreStringValue,
    val height: FirestoreDoubleValue,
    val weight: FirestoreDoubleValue
)

data class FirestoreStringValue(
    val stringValue: String
)
data class FirestoreIntegerValue(
    val integerValue: Int
)
data class FirestoreDoubleValue(
    val doubleValue: Float
)