package com.example.cocobeat.model

import androidx.lifecycle.*
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.repository.ReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ReadingViewModel(private val repository: ReadingRepository) : ViewModel(){
    var monthData: LiveData<List<Reading>>? = null
    var lastReading: LiveData<Reading> = repository.lastReading

    fun loadMonthData(startDate: Date, endDate: Date) {
        monthData = repository.getMonthData(startDate, endDate)
    }

    fun insertReadings(readings: MutableList<Reading>){
        repository.insertReadings(readings)
    }
}