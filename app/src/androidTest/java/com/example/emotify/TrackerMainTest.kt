package com.example.emotify


import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.emotify.View.trackerMain
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackerMainTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(trackerMain::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testHappyButtonNavigatesToTrackerCalendar() {
        Espresso.onView(withId(R.id.happyButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent("com.example.emotify.View.trackerCalendar"))
        Intents.intended(IntentMatchers.hasExtra("emotion", "Happy"))
    }

    @Test
    fun testSadButtonNavigatesToTrackerCalendar() {
        Espresso.onView(withId(R.id.sadButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent("com.example.emotify.View.trackerCalendar"))
        Intents.intended(IntentMatchers.hasExtra("emotion", "Sad"))
    }

    @Test
    fun testNeutralButtonNavigatesToTrackerCalendar() {
        Espresso.onView(withId(R.id.neutralButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent("com.example.emotify.View.trackerCalendar"))
        Intents.intended(IntentMatchers.hasExtra("emotion", "Neutral"))
    }

    @Test
    fun testAngryButtonNavigatesToTrackerCalendar() {
        Espresso.onView(withId(R.id.angryButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent("com.example.emotify.View.trackerCalendar"))
        Intents.intended(IntentMatchers.hasExtra("emotion", "Angry"))
    }

    @Test
    fun testScaredButtonNavigatesToTrackerCalendar() {
        Espresso.onView(withId(R.id.scaredButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent("com.example.emotify.View.trackerCalendar"))
        Intents.intended(IntentMatchers.hasExtra("emotion", "Scared"))
    }

    @Test
    fun testSurprisedButtonNavigatesToTrackerCalendar() {
        Espresso.onView(withId(R.id.surprisedButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent("com.example.emotify.View.trackerCalendar"))
        Intents.intended(IntentMatchers.hasExtra("emotion", "Surprised"))
    }
}
