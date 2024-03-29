package com.example.cocobeat

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cocobeat.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.rules.ActivityScenarioRule


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_isActivityInView(){
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_month_picker() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.month_picker_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_chart() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.chart)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_button() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_devices)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_title() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.main_title)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_arrowBack() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.img_arrow_back)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_arrowNext() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.img_arrow_next)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navMainActivity(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_devices)).perform(click())
        onView(withId(R.id.devices)).check(matches(isDisplayed()))
    }
}