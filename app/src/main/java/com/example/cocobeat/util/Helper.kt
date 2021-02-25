package com.example.cocobeat.util


import java.util.*

class Helper {
     fun calculateAverage(marks: List<Double?>): Double {
        var sum = 0.0

        if (marks.isNotEmpty()) {
            for (mark in marks) {
                sum += mark!!
            }
            return sum / marks.size
        }
        return sum
    }

     fun getDate(year: Int, monthNumber: Int, hour: Int, minute: Int, second: Int, getMin: Boolean) : Calendar {
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(Calendar.YEAR, year)
        startDate.set(Calendar.MONTH, monthNumber)
        startDate.set(Calendar.HOUR, hour)
        startDate.set(Calendar.MINUTE, minute)
        startDate.set(Calendar.SECOND, second)
         startDate.set(Calendar.MILLISECOND, second)
         if(getMin){
            startDate.set(Calendar.DAY_OF_MONTH, startDate.getActualMinimum(Calendar.DAY_OF_MONTH))
        }else{
            startDate.set(Calendar.DAY_OF_MONTH, startDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        return startDate
    }


}