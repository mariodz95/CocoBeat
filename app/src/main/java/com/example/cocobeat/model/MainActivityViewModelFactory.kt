package com.example.cocobeat.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.repository.OpenWeatherRepository

class MainActivityViewModelFactory constructor(private val repository: OpenWeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java!!)) {
            MainActivityViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}