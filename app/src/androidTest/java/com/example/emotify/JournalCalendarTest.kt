package com.example.emotify

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.emotify.R
import com.example.emotify.View.journalCalendar
import com.example.emotify.View.journalEntry
import org.hamcrest.Matchers.containsString

@RunWith(AndroidJUnit4::class)
class JournalCalendarTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testSelectingDateOpensJournalEntry() {
        ActivityScenario.launch(journalCalendar::class.java).use {
            onView(withId(R.id.calendarView)).perform(click())
            Intents.intended(IntentMatchers.hasComponent(journalEntry::class.java.name))
        }
    }

    @Test
    fun testJournalEntryDisplaysCorrectDateAndSaves() {
        val testDate = "2025-03-02"
        val intent = Intent(ApplicationProvider.getApplicationContext(), journalEntry::class.java).apply {
            putExtra("selectedDate", testDate)
        }

        ActivityScenario.launch<journalEntry>(intent).use {
            onView(withId(R.id.journalDate)).check(matches(withText(containsString(testDate))))
            onView(withId(R.id.journalEntry)).perform(click())
            onView(withId(R.id.saveButton)).perform(click())
        }
    }
}
