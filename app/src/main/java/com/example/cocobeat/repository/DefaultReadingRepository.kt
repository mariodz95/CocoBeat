package com.example.cocobeat.repository

import androidx.lifecycle.LiveData
import com.example.cocobeat.database.entity.Reading
import java.util.*

interface DefaultReadingRepository {
    fun getLastReading(): LiveData<Reading>
    fun getMonthData(startDate: Date, endDate: Date)  : LiveData<List<Reading>>?
    fun insertReadings(readings: MutableList<Reading>)
}