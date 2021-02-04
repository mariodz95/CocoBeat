package com.example.cocobeat.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cocobeat.DAO.ReadingDao
import com.example.cocobeat.Entities.Reading

@Database(entities = arrayOf(Reading::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ReadingDao
}