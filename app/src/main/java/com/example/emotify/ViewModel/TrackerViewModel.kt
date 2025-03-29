package com.example.emotify.ViewModel

import androidx.lifecycle.ViewModel
import com.example.emotify.Model.TrackerRepository
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel responsible for tracking and logging user emotions.
 * It communicates with the TrackerRepository to store or update emotion data.
 */
class TrackerViewModel : ViewModel() {
    private val repository = TrackerRepository()

    /**
     * Logs the detected emotion for the current date.
     *
     * @param emotion The emotion detected by the system.
     */
    fun logEmotion(emotion: String) {
        val currentDate = getCurrentDate()
        repository.saveOrUpdateEmotion(currentDate, emotion)
    }

    /**
     * Gets the current date in "yyyy-MM-dd" format.
     *
     * @return A string representing today's date.
     */
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
