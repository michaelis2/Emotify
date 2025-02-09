package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emotify.R
import androidx.activity.viewModels
import com.example.emotify.ViewModel.TrackerViewModel

class trackerMain : AppCompatActivity() {
    private val trackerViewModel: TrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracker_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val happyButton = findViewById<ImageButton>(R.id.happyButton)
        val sadButton = findViewById<ImageButton>(R.id.sadButton)
        val neutralButton = findViewById<ImageButton>(R.id.neutralButton)
        val angryButton = findViewById<ImageButton>(R.id.angryButton)
        val scaredButton = findViewById<ImageButton>(R.id.scaredButton)
        val surprisedButton = findViewById<ImageButton>(R.id.surprisedButton)


        // Function to handle emotion click and navigate to trackerCalendar
        fun onEmotionClicked(emotion: String) {
            trackerViewModel.logEmotion(emotion)
            val intent = Intent(this, trackerCalendar::class.java)
            intent.putExtra("emotion", emotion) // Pass the selected emotion
            startActivity(intent)
        }

        // Setting up click listeners for emotion buttons
        happyButton.setOnClickListener {
            trackerViewModel.logEmotion("Happy")
            navigateToTrackerCalendar("Happy")
        }
        sadButton.setOnClickListener {
            trackerViewModel.logEmotion("Sad")
            navigateToTrackerCalendar("Sad")
        }
        neutralButton.setOnClickListener {
            trackerViewModel.logEmotion("Neutral")
            navigateToTrackerCalendar("Neutral")
        }
        angryButton.setOnClickListener {
            trackerViewModel.logEmotion("Angry")
            navigateToTrackerCalendar("Angry")
        }
        scaredButton.setOnClickListener {
            trackerViewModel.logEmotion("Scared")
            navigateToTrackerCalendar("Scared")
        }
        surprisedButton.setOnClickListener {
            trackerViewModel.logEmotion("Surprised")
            navigateToTrackerCalendar("Surprised")
        }
    }
    private fun navigateToTrackerCalendar(emotion: String) {
        val intent = Intent(this, trackerCalendar::class.java)
        intent.putExtra("emotion", emotion)  // Pass emotion data to trackerCalendar
        startActivity(intent)
    }
    }
