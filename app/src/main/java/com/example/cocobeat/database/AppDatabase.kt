package com.example.cocobeat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cocobeat.database.dao.*
import com.example.cocobeat.database.entity.*
import com.example.cocobeat.util.Converters

@Database(entities = [Reading::class, Device::class, Exercise::class, Step::class, Food::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
    abstract fun deviceDao(): DeviceDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun stepDao(): StepDao
    abstract fun foodDao(): FoodDao
}