package com.example.cocobeat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Device(
        @PrimaryKey val id: UUID = UUID.randomUUID(),
        @ColumnInfo(name = "serial_number_id") val serialNumberId: String?,
        @ColumnInfo(name = "last_sync_date") val lastSyncDate: Date?,
        @ColumnInfo(name = "name") val name: String?,
        val imageResource: Int
)
