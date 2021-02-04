package com.example.cocobeat.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Reading(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "id_device") val idDevice: String?,
    @ColumnInfo(name = "date_added") val dateAdded: Date?,
    @ColumnInfo(name = "value") val value: String?
)
