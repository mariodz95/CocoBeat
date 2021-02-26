package com.example.cocobeat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.repository.DeviceRepository

class DeviceViewModel(private val repository: DeviceRepository) : ViewModel() {
    var allDevices: LiveData<List<Device>> = repository.allDevices

    fun insertDevice(device: Device){
        repository.insertDevice(device)
    }
}