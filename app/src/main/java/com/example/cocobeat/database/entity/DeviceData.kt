package com.example.cocobeat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Reading(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "id_device") val idDevice: String?,
    @ColumnInfo(name = "date_added") val dateAdded: Date?,
    @ColumnInfo(name = "value") val value: Double?,
    @ColumnInfo(name = "units") val units: String?,
    @ColumnInfo(name = "readingDate") val readingDate: Date?
)
