package com.example.cocobeat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Food(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "food_name") val foodName: String?,
    @ColumnInfo(name = "calories") val calories: Int?,
    @ColumnInfo(name = "image_path") val imagePath: String?,
    @ColumnInfo(name = "date_added") val dateAdded: Date?,
    @ColumnInfo(name = "time_added") val value: String?
)
