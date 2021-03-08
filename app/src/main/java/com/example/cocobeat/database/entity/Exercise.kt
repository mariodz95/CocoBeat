package com.example.cocobeat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Exercise(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "exercise_name") val exerciseName: String?,
    @ColumnInfo(name = "hour_duration") val hourDuration: Int?,
    @ColumnInfo(name = "minute_duration") val minuteDuration: Int?,
    @ColumnInfo(name = "date_added") val dateAdded: Date?,
    @ColumnInfo(name = "time_added") val value: String?
)
