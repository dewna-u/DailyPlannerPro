package com.example.dailyplannerpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskNameInput: EditText
    private lateinit var taskDescriptionInput: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var reminderPicker: TimePicker
    private lateinit var timerDurationInput: EditText
    private var isEditing: Boolean = false
    private lateinit var currentTask: Task
    private var taskPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskNameInput = findViewById(R.id.task_name_input)
        taskDescriptionInput = findViewById(R.id.task_description_input)
        prioritySpinner = findViewById(R.id.priority_spinner)
        reminderPicker = findViewById(R.id.reminder_picker)
        timerDurationInput = findViewById(R.id.timer_duration_input)
        val saveTaskButton = findViewById<Button>(R.id.save_task_button)
        val backButton = findViewById<Button>(R.id.back_button)

        // Check if we are editing an existing task
        intent.getSerializableExtra("task")?.let {
            currentTask = it as Task
            taskNameInput.setText(currentTask.name)
            taskDescriptionInput.setText(currentTask.description)
            val priorityArray = resources.getStringArray(R.array.priority_array)
            prioritySpinner.setSelection(priorityArray.indexOf(currentTask.priority))
            reminderPicker.setHour(currentTask.reminderTime.toInt())
            reminderPicker.setMinute((currentTask.reminderTime % 60).toInt())
            timerDurationInput.setText((currentTask.timerDuration / 60000).toString())  // Convert to minutes
            isEditing = true
            taskPosition = intent.getIntExtra("position", -1)
        }

        saveTaskButton.setOnClickListener {
            val name = taskNameInput.text.toString().trim()
            val description = taskDescriptionInput.text.toString().trim()
            val priority = prioritySpinner.selectedItem.toString()

            // Get reminder time and timer duration
            val reminderTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, reminderPicker.hour)
                set(Calendar.MINUTE, reminderPicker.minute)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            val timerDuration = timerDurationInput.text.toString().toLongOrNull()?.let { it * 60000 } ?: 0L // Convert to milliseconds

            // Validate inputs
            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new task or update the existing one
            val task = Task(name, description, priority, reminderTime, timerDuration)
            val resultIntent = Intent().apply {
                putExtra("task", task)
                putExtra("isEditing", isEditing)
                putExtra("position", taskPosition)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
