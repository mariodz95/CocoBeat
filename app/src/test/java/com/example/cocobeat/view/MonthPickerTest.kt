package com.example.cocobeat.view

import android.app.Activity
import android.os.Build
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.*


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MonthPickerTest {
    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity
    private lateinit var monthPicker: MonthPicker

    @Before
    fun setup() {
        // Create an activity (Can be any sub-class: i.e. AppCompatActivity, FragmentActivity, etc)
        activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()

        // Create the view using the activity context
        monthPicker = MonthPicker(activity)

    }

    @Test
    fun monthPicker_GetMonthForInt_ReturnMonthNameForGivenNumber() {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 0)
        val monthDate = SimpleDateFormat("MMMM")
        val monthName: String = monthDate.format(cal.time)
        val value = monthPicker.getMonthForInt(0)

        assertThat(value).isEqualTo(monthName)
    }
}
