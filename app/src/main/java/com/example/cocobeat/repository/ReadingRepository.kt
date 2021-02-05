package com.example.cocobeat.repository

import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading


class ReadingRepository(private val readingDao: ReadingDao){
    val allData: LiveData<List<Reading>> = readingDao.getAllReadings()

    suspend fun addReadings(readings: Reading){
        readingDao.insertReadings(readings)
    }
}