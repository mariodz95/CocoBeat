package com.example.cocobeat.DAO

import androidx.room.*
import com.example.cocobeat.Entities.Reading

@Dao
interface ReadingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg readings: Reading)
}