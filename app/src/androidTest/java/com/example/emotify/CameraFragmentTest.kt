package com.example.emotify

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.example.emotify.View.MainActivity
import com.example.emotify.View.cameraResult
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CameraFragmentTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun test_CameraPreview_IsDisplayed() {
        onView(withId(R.id.viewFinder))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_CaptureButton_IsDisplayed() {
        onView(withId(R.id.image_capture_button))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_CaptureButton_Opens_ResultActivity() {
        onView(withId(R.id.image_capture_button)).perform(click())

        // Simulate a short delay for processing
        Thread.sleep(3000)

        Intents.intended(hasComponent(cameraResult::class.java.name))
    }
}
