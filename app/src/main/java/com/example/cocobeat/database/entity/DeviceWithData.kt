package com.example.cocobeat.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DeviceWithData(
        @Embedded val user: Device,
        @Relation(
                parentColumn = "serialNumberId",
                entityColumn = "idDevice"
        )
        val readingList: List<Reading>
)
