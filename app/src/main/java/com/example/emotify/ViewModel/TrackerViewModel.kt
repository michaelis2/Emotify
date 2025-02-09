package com.example.emotify.ViewModel

import androidx.lifecycle.ViewModel
import com.example.emotify.Model.TrackerRepository
import java.text.SimpleDateFormat
import java.util.*

class TrackerViewModel : ViewModel() {
    private val repository = TrackerRepository()

    fun logEmotion(emotion: String) {
        val currentDate = getCurrentDate()
        repository.saveOrUpdateEmotion(currentDate, emotion)
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
