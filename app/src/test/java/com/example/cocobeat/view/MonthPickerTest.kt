package com.example.cocobeat.view

import android.content.Context
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class MonthPickerTest {
    private lateinit var monthPicker: MonthPicker

    @Before
    fun setup() {
        val mContextMock = mockk<Context>(relaxed = true)
        monthPicker = MonthPicker(mContextMock, null, 0, 0)
    }

    @Test
    fun monthPicker_GetMonthForInt_ReturnMonthNameForGivenNumber() {
    }

}
