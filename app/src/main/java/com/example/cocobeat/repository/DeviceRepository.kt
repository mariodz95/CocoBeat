package com.example.cocobeat.repository

import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.DeviceDao
import com.example.cocobeat.database.entity.Device

class DeviceRepository(private val deviceDao: DeviceDao) {
    var allDevices: LiveData<List<Device>> = deviceDao.getAllDevices()

    fun insertDevice(device: Device){
        deviceDao.insertDevice(device)
    }
}