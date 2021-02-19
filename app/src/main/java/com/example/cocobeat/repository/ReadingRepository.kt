package com.example.cocobeat.repository

import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading
import java.lang.Exception
import java.util.*


class ReadingRepository(private val readingDao: ReadingDao){
    var allData: LiveData<List<Reading>>? = null

     fun getMonthData(startDate: Date, endDate: Date)  : LiveData<List<Reading>>? {
        allData = readingDao.getAllReadings(startDate, endDate)
        return allData
    }

     fun insertReadings(readings: MutableList<Reading>){
        readingDao.insertReadings(readings)
    }
}