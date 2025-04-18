package com.example.emotify


import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.emotify.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.emotify.View.cameraResult
import com.example.emotify.View.trackerCalendar

@RunWith(AndroidJUnit4::class)
class CameraResultTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant("android.permission.READ_EXTERNAL_STORAGE")

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testCameraResultDisplaysImageAndEmotion() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), cameraResult::class.java).apply {
            putExtra("imagePath", "/storage/emulated/0/Pictures/test_image.jpg")
            putExtra("emotion", "Happy")
        }

        ActivityScenario.launch<cameraResult>(intent).use {
            onView(withId(R.id.imageView3)).check(matches(isDisplayed()))
            onView(withId(R.id.emotion_result)).check(matches(withText("Happy")))
        }
    }

    @Test
    fun testUploadButtonNavigatesToTrackerCalendar() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), cameraResult::class.java).apply {
            putExtra("imagePath", "/storage/emulated/0/Pictures/test_image.jpg")
            putExtra("emotion", "Happy")
        }

        ActivityScenario.launch<cameraResult>(intent).use {
            onView(withId(R.id.saveImage)).perform(click())
            Intents.intended(IntentMatchers.hasComponent(trackerCalendar::class.java.name))
        }
    }
}