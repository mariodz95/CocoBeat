package com.example.cocobeat.repository


import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading
import java.util.*


class ReadingRepository(private val readingDao: ReadingDao){
    var allData: LiveData<List<Reading>>? = null
    var lastReading: LiveData<Reading> = readingDao.getLastReading()

    var readings: LiveData<List<Reading>>? = null

     fun getMonthData(startDate: Date, endDate: Date) : LiveData<List<Reading>>? {
        allData = readingDao.getAllReadings(startDate, endDate)
        return allData
    }

    fun insertReadings(readings: MutableList<Reading>){
          readingDao.insertReadings(readings)
    }

    fun getAllReadings() : LiveData<List<Reading>>? {
        readings = readingDao.getReadings()
        return readings
    }
}
