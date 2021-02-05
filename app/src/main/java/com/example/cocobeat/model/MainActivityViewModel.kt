package com.example.cocobeat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.repository.ReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: ReadingRepository) : ViewModel(){

    private val allData: LiveData<List<Reading>>

    init {
        allData = repository.allData
    }

    fun addReadings(readings: Reading){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReadings(readings)
        }
    }
}