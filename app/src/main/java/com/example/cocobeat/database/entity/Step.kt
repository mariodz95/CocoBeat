package com.example.cocobeat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Step(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "steps") val steps: Int?,
    @ColumnInfo(name = "date_added") val dateAdded: Date?,
    @ColumnInfo(name = "date_started") val dateStarted: String?,
    @ColumnInfo(name = "date_ended") val dateEnded: String?
)
