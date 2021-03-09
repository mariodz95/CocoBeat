package com.example.cocobeat.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cocobeat.database.entity.Reading
import java.util.*

@Dao
interface ReadingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReadings(readings: MutableList<Reading>)

    @Query("SELECT * FROM reading WHERE readingDate BETWEEN :startDate AND :endDate")
    fun getAllReadings(startDate: Date, endDate: Date) : LiveData<List<Reading>>

    @Query("SELECT * FROM reading ORDER BY ID DESC LIMIT 1")
    fun getLastReading() : LiveData<Reading>

    @Query("SELECT * FROM reading ORDER BY date_added DESC")
    fun getReadings() : LiveData<List<Reading>>
}