package com.example.cocobeat.repository


import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading
import java.util.*


class ReadingRepository(private val readingDao: ReadingDao) : DefaultReadingRepository{
    var allData: LiveData<List<Reading>>? = null
    private lateinit var lastReading: LiveData<Reading>

    override fun getLastReading(): LiveData<Reading> {
        lastReading = readingDao.getLastReading()
        return lastReading
    }

    override fun getMonthData(startDate: Date, endDate: Date) : LiveData<List<Reading>>? {
        allData = readingDao.getAllReadings(startDate, endDate)
        return allData
    }

     override fun insertReadings(readings: MutableList<Reading>){
        readingDao.insertReadings(readings)
    }
}
