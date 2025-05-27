package com.example.dailyplannerpro

import java.io.Serializable

data class Task(
    var name: String,
    var description: String,
    var priority: String,
    var reminderTime: Long = 0L,  // Time in milliseconds
    var timerDuration: Long = 0L   // Duration in milliseconds
) : Serializable
