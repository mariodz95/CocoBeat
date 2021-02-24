package com.example.cocobeat

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cocobeat.activity.DevicesActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DevicesActivityTest {

    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.devices)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_recyclerView() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)

        onView(withId(R.id.devices)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_button() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.devices)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add_device)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_title() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.devices_title)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_deviceImage() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.device_image)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_deviceName() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.device_name)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_DeviceSyncDate() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.device_sync_date)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_OptionsBtn() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.options_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_backButton() {
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_openDialog(){
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.button_add_device)).perform(click())
        onView(withId(R.id.dialog)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_dialogRecyclerView(){
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.button_add_device)).perform(click())
        onView(withId(R.id.recycler_view_dialog)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_buttonCancel(){
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.button_add_device)).perform(click())
        onView(withId(R.id.button_cancel)).check(matches(isDisplayed()))
    }

    @Test
    fun test_click_buttonCancel(){
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.button_add_device)).perform(click())
        onView(withId(R.id.button_cancel)).perform(click())
        onView(withId(R.id.dialog)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_navDevicesActivity(){
        val activityScenario = ActivityScenario.launch(DevicesActivity::class.java)
        onView(withId(R.id.toolbar)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.toolbar)).perform(click())
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
}