package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import com.example.emotify.R

/**
 * Activity that allows users to select a date from a calendar and navigate
 * to the journal entry page for that specific date.
 */

class journalCalendar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // Set a listener to detect date selection on the calendar
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"  // Format: YYYY-MM-DD
            val intent = Intent(this, journalEntry::class.java)

            // Pass the selected date as an extra to the next activity
            intent.putExtra("selectedDate", selectedDate)

            // Start the journalEntry activity
            startActivity(intent)
        }
    }
}
