package com.example.cocobeat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cocobeat.database.dao.DeviceDao
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.util.Converters

@Database(entities = [Reading::class, Device::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
    abstract fun deviceDao(): DeviceDao
}