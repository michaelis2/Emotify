package com.example.emotify.View

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.emotify.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

/**
 * The TrackerHistory activity displays a line chart representing the user's emotions over time.
 * It retrieves emotion data from Firestore and maps it to visualized points on a chart.
 */

class trackerHistory : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())  // Date format for x-axis labels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracker_history)

        lineChart = findViewById(R.id.emotionLineChart) // Initialize the line chart view
        setupLineChart()// Set up the appearance and behavior of the line chart
        fetchEmotionData() // Fetch and display the user's emotion data
    }

    /**
     * Configures the appearance and formatting of the line chart.
     */
    private fun setupLineChart() {
        lineChart.description.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // Set x-axis position
        lineChart.xAxis.granularity = 1f  // Ensure labels are evenly spaced
        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val date = Date(value.toLong())
                return dateFormat.format(date)  // Convert timestamp to "dd-MM" format
            }
        }
        lineChart.axisRight.isEnabled = false
        lineChart.setDrawGridBackground(false)

        // Customize y-axis labels with emotion emojis
        lineChart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value.toInt()) {
                    1 -> "😡"
                    2 -> "😢"
                    3 -> "😐"
                    4 -> "😲"
                    5 -> "😊"
                    else -> "❓"
                }
            }
        }
    }

    /**
     * Fetches emotion data from Firestore and updates the chart.
     */
    private fun fetchEmotionData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("TrackerHistory", "User not authenticated")
            return
        }

        // Get the timestamp for 30 days ago to filter recent data
        val thirtyDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -30)
        }.time

        // Query Firestore for emotion data in the last 30 days
        db.collection("images")
            .document(userId)
            .collection("userImages")
            .whereGreaterThanOrEqualTo("timestamp", thirtyDaysAgo.time)
            .get()
            .addOnSuccessListener { documents ->
                val entries = mutableListOf<Entry>()

                for (document in documents) {
                    val timestamp = document.getLong("timestamp") ?: continue
                    val emotion = getEmotionValue(document.getString("emotion") ?: "neutral")
                    val emotionEmoji = getEmotionEmoji(document.getString("emotion") ?: "neutral")
                    val emotionValue = when (emotionEmoji) {
                        "😊" -> 5
                        "😢" -> 2
                        "😡" -> 1
                        "😱" -> 3
                        "😲" -> 4
                        "😐" -> 3
                        else -> 0
                    }
                    entries.add(Entry(timestamp.toFloat(), emotionValue.toFloat()))  // Use raw timestamp for the x-axis
                }

                val dataSet = LineDataSet(entries, "Emotions Over Time").apply {
                    color = getColor(R.color.army)
                    setCircleColor(getColor(R.color.black))
                    lineWidth = 2f
                    circleRadius = 4f
                }

                lineChart.data = LineData(dataSet)
                lineChart.invalidate()  // Refresh the chart
            }
            .addOnFailureListener { e ->
                Log.e("TrackerHistory", "Error fetching data", e)
            }
    }

    /**
     * Converts an emotion string into a corresponding numerical value for chart plotting.
     * @param emotion The detected emotion as a string.
     * @return An integer value representing the emotion.
     */
    private fun getEmotionValue(emotion: String): Int {
        return when (emotion.lowercase(Locale.ROOT)) {
            "happy" -> 5
            "sad" -> 2
            "angry" -> 1
            "fear" -> 3
            "surprise" -> 4
            "neutral" -> 3
            else -> 0
        }
    }

    /**
    * Maps an emotion string to a corresponding emoji.
    * @param emotion The detected emotion as a string.
    * @return A string representing the emoji for the given emotion.
    */
    private fun getEmotionEmoji(emotion: String): String {
        return when (emotion.lowercase(Locale.ROOT)) {
            "happy" -> "😊"
            "sad" -> "😢"
            "angry" -> "😡"
            "fear" -> "😱"
            "surprise" -> "😲"
            "neutral" -> "😐"
            else -> "❓"
        }
    }

}
