package com.example.cocobeat.util


import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromUUID(uid: UUID?): String? {

        if (uid != null)
            return uid.toString()
        return null
    }

    @TypeConverter
    fun toUUID(str: String?): UUID? {

        if (str != null) {
            return UUID.fromString(str)
        }
        return null
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}