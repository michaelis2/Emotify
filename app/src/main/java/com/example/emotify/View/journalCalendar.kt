package com.example.emotify.View

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import com.example.emotify.R

class journalCalendar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"  // Format: YYYY-MM-DD
            val intent = Intent(this, journalEntry::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }
    }
}
