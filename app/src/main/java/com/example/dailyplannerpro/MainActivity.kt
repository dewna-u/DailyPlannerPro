package com.example.dailyplannerpro

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private var tasks: ArrayList<Task> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check and request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        // Load saved tasks from SharedPreferences
        tasks = TaskStorage.loadTasks(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(this, tasks)
        recyclerView.adapter = taskAdapter

        // Set click listener for Add Task button
        findViewById<Button>(R.id.add_task_button).setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, 1) // Request code 1 for adding a task
        }

        
    }

    private fun setReminderAlarm() {
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + 60000 // 1 minute from now

        // Set the alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val newTask = data.getSerializableExtra("task") as Task
            tasks.add(newTask)
            taskAdapter.notifyItemInserted(tasks.size - 1) // Update the UI
            TaskStorage.saveTasks(this, tasks) // Save updated list to SharedPreferences
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            val editedTask = data.getSerializableExtra("task") as Task
            val position = data.getIntExtra("position", -1)

            if (position != -1) {
                // Update the task in the list at the correct position
                tasks[position] = editedTask
                taskAdapter.notifyItemChanged(position) // Update the UI
                TaskStorage.saveTasks(this, tasks) // Save updated list to SharedPreferences
            }
        }
    }
}
