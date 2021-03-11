package com.example.cocobeat.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cocobeat.database.entity.Device

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevice(device: Device)

    @Query("SELECT * FROM device")
    fun getAllDevices() : LiveData<List<Device>>

    @Delete
    fun deleteDevice(device: Device)
}