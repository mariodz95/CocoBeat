package com.example.cocobeat.model

import androidx.lifecycle.*
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.repository.ReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ReadingViewModel(private val repository: ReadingRepository) : ViewModel(){
    var  monthData: LiveData<List<Reading>>? = null

    fun loadMonthData(startDate: Date, endDate: Date) : LiveData<List<Reading>>? {
            monthData = repository.getMonthData(startDate, endDate)
            return monthData
    }

    fun insertReadings(readings: MutableList<Reading>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertReadings(readings)
        }
    }
}