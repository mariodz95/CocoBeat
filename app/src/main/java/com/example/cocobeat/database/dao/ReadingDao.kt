package com.example.cocobeat.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cocobeat.database.entity.Reading

@Dao
interface ReadingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReadings(vararg readings: Reading)

    @Query("SELECT * FROM reading")
    fun getAllReadings() : LiveData<List<Reading>>
}