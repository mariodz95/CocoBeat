package com.example.cocobeat

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DateViewModel : ViewModel() {
    var c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month_date = SimpleDateFormat("MMMM")
    var month_name = month_date.format(c.getTime())

    fun getYear(): Int? {
        return year
    }

    fun getMonth(): String? {
        return month_name
    }

    fun getNextMonth(): String {
        c.add(Calendar.MONTH, 1);
        val thisMonth: Int = c.get(Calendar.MONTH)
        if(thisMonth == 0)
        {
            year = c.get(Calendar.YEAR)
        }
        month_name = month_date.format(c.getTime())
        return month_name
    }

    fun getPrevMonth(): String {
        c.add(Calendar.MONTH, -1);
        val thisMonth: Int = c.get(Calendar.MONTH)
        if(thisMonth == 11)
        {
            year = c.get(Calendar.YEAR)
        }
        month_name = month_date.format(c.getTime())
        return month_name
    }
}