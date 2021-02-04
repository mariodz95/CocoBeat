package com.example.cocobeat.model

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel : ViewModel() {
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var MONTH_DATE = SimpleDateFormat("MMMM")
    private var monthName = MONTH_DATE.format(calendar.getTime())

    fun getYear(): Int? {
        return year
    }

    fun getMonth(): String? {
        return monthName
    }

    fun getNextMonth(): String {
        calendar.add(Calendar.MONTH, 1);
        val thisMonth: Int = calendar.get(Calendar.MONTH)
        if(thisMonth == 0)
        {
            year = calendar.get(Calendar.YEAR)
        }
        monthName = MONTH_DATE.format(calendar.getTime())
        return monthName
    }

    fun getPrevMonth(): String {
        calendar.add(Calendar.MONTH, -1);
        val thisMonth: Int = calendar.get(Calendar.MONTH)
        if(thisMonth == 11)
        {
            year = calendar.get(Calendar.YEAR)
        }
        monthName = MONTH_DATE.format(calendar.getTime())
        return monthName
    }
}