package com.example.dailyplannerpro

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private var startTime: Long = 0
    private var timeInMillis: Long = 0
    private var handler: Handler = Handler()
    private var isRunning: Boolean = false

    private val runnable = object : Runnable {
        override fun run() {
            timeInMillis = SystemClock.elapsedRealtime() - startTime
            val seconds = (timeInMillis / 1000) % 60
            val minutes = (timeInMillis / (1000 * 60)) % 60
            val milliseconds = (timeInMillis % 1000) / 10

            timerTextView.text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
            handler.postDelayed(this, 10)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.stopwatch)

        findViewById<Button>(R.id.start_button).setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.elapsedRealtime() - timeInMillis
                handler.postDelayed(runnable, 0)
                isRunning = true
            }
        }

        findViewById<Button>(R.id.stop_button).setOnClickListener {
            if (isRunning) {
                handler.removeCallbacks(runnable)
                isRunning = false
            }
        }

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            handler.removeCallbacks(runnable)
            timeInMillis = 0
            timerTextView.text = "00:00:00"
            isRunning = false
        }

        findViewById<Button>(R.id.back_button).setOnClickListener {
            finish()  // Closes the current activity and returns to the previous one
        }
    }
}
