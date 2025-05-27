package com.example.dailyplannerpro

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private var pauseOffset: Long = 0
    private var running: Boolean = false
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val task = intent.getSerializableExtra("task") as Task

        val taskNameTextView = findViewById<TextView>(R.id.task_name_detail)
        val taskDescriptionTextView = findViewById<TextView>(R.id.task_description_detail)
        val taskPriorityTextView = findViewById<TextView>(R.id.task_priority_detail)
        chronometer = findViewById(R.id.stopwatch)

        taskNameTextView.text = task.name
        taskDescriptionTextView.text = task.description
        taskPriorityTextView.text = task.priority

        // Setup timer if duration is set
        if (task.timerDuration > 0) {
            setupTimer(task.timerDuration)
        }

        findViewById<Button>(R.id.start_button).setOnClickListener {
            if (!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
            }
        }

        findViewById<Button>(R.id.stop_button).setOnClickListener {
            if (running) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                running = false
            }
        }

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0
        }

        findViewById<Button>(R.id.back_button).setOnClickListener {
            finish()
        }
    }

    private fun setupTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                findViewById<TextView>(R.id.timer_text).text = String.format("%02d:%02d", seconds / 60, seconds % 60)
            }

            override fun onFinish() {
                Toast.makeText(this@TaskDetailActivity, "Timer Finished!", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.timer_text).text = "00:00"

                // Play the sound
                val soundUri = Uri.parse("android.resource://${packageName}/raw/sound")
                val mediaPlayer = MediaPlayer.create(this@TaskDetailActivity, soundUri)
                mediaPlayer.start()

                // Vibrate the device
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // Vibrate for 500 milliseconds
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }
}
