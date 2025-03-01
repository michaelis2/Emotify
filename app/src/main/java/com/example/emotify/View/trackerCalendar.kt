package com.example.emotify.View

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emotify.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class trackerCalendar : AppCompatActivity() {


    private lateinit var calendarView: CalendarView
    private lateinit var moodTextView: TextView
    private lateinit var imageView: ImageView
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker_calendar)

        calendarView = findViewById(R.id.calendarView2)
        moodTextView = findViewById(R.id.moodTextview)
        imageView = findViewById(R.id.imageView13)

        // Listener for date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = formatDate(year, month, dayOfMonth)
            fetchImageAndEmotion(selectedDate)
        }
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        // Convert selected date to YYYY-MM-DD format (or adjust to match Firestore storage format)
        return String.format("%04d-%02d-%02d", year, month + 1, day)
    }
    private fun fetchImageAndEmotion(date: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return

        // Query Firestore for images stored on the selected date
        firebaseFirestore.collection("images")
            .document(userId)
            .collection("userImages")
            .whereGreaterThanOrEqualTo("timestamp", getStartOfDayTimestamp(date))
            .whereLessThan("timestamp", getEndOfDayTimestamp(date))
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {

                        val emotion = document.getString("emotion") ?: "No emotion recorded"
                        val imageBase64 = document.getString("imageData") ?: ""

                        // Update UI
                        val emoji = getEmojiForEmotion(emotion)
                        moodTextView.text = "Mood: $emotion $emoji"
                        displayImage(imageBase64)
                        break // Display only the first result
                    }
                } else {
                    moodTextView.text = " "
                    imageView.setImageResource(R.drawable.nomood) // Default image
                }
            }
            .addOnFailureListener {
                moodTextView.text = "Failed to load data"
            }
    }

    private fun displayImage(base64String: String) {
        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            moodTextView.text = "Error loading image"
        }
    }

    private fun getStartOfDayTimestamp(date: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            time = sdf.parse(date)!!
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun getEndOfDayTimestamp(date: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            time = sdf.parse(date)!!
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }
    private fun getEmojiForEmotion(emotion: String): String {
        return when (emotion) {
            "Happy" -> "😊"
            "Sad" -> "😢"
            "Neutral" -> "😐"
            "Angry" -> "😡"
            "Scared" -> "😨"
            "Surprised" -> "😲"
            else -> "❓" // Default emoji for unknown emotions
        }
    }

}