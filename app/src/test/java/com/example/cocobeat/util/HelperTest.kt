package com.example.cocobeat.util


import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class HelperTest {

    private lateinit var helper: Helper

    @Before
    fun setup() {
        helper = Helper()
    }

    @Test
    fun helper_CalculateAverage_ReturnsAverageNumberOfList() {
        val list = listOf<Double>(1.0, 2.0, 3.0, 4.0, 5.0)
        val average = 3.0

        var calculatedAverage = helper.calculateAverage(list)

        assertEquals(calculatedAverage, average)
    }

    @Test
    fun helper_GetDate_ReturnsDateByGivenVariables(){
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.MONTH, 1)
        cal.set(Calendar.YEAR, 2021)
        cal.set(Calendar.HOUR, 1)
        cal.set(Calendar.MINUTE, 1)
        cal.set(Calendar.SECOND, 1)
        cal.set(Calendar.MILLISECOND, 1)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))

        var helperDate = helper.getDate(2021, 1, 1, 1, 1,  false)

        assertEquals(cal, helperDate)
    }
}