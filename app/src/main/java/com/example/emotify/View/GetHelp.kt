package com.example.emotify.View

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emotify.R
import android.util.Log

class GetHelp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_get_help)

        val countryCode = intent.getStringExtra("COUNTRY_CODE") ?: "MY"

        Log.d("CountryCode", "Retrieved country code: $countryCode")
        // Get references to the TextViews
        val emergencyTextView = findViewById<TextView>(R.id.ambulance)
        val emotionalSupportTextView = findViewById<TextView>(R.id.emotionalsupporthotline)
        val womenSupportTextView = findViewById<TextView>(R.id.womenssupporthotline)
        val narcoticsSupportTextView = findViewById<TextView>(R.id.narcoticssupporthotline)

        // Determine the correct hotlines
        val hotlineNumber = when (countryCode) {
            "MY" -> getString(R.string.hotline_my)
            "UK" -> getString(R.string.hotline_uk)
            "US" -> getString(R.string.hotline_us)
            else -> getString(R.string.hotline_default)
        }

        val emotionalSupportNumber = when (countryCode) {
            "MY" -> getString(R.string.emotional_support_my)
            "UK" -> getString(R.string.emotional_support_uk)
            "US" -> getString(R.string.emotional_support_us)
            else -> getString(R.string.emotional_support_default)
        }

        val womenSupportNumber = when (countryCode) {
            "MY" -> getString(R.string.women_support_my)
            "UK" -> getString(R.string.women_support_uk)
            "US" -> getString(R.string.women_support_us)
            else -> getString(R.string.women_support_default)
        }

        val narcoticsSupportNumber = when (countryCode) {
            "MY" -> getString(R.string.narcotics_support_my)
            "UK" -> getString(R.string.narcotics_support_uk)
            "US" -> getString(R.string.narcotics_support_us)
            else -> getString(R.string.narcotics_support_default)
        }

        // Update the TextViews
        emergencyTextView.text = hotlineNumber
        emotionalSupportTextView.text = emotionalSupportNumber
        womenSupportTextView.text = womenSupportNumber
        narcoticsSupportTextView.text = narcoticsSupportNumber

        // Ensure UI insets work properly
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
