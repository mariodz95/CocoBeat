package com.example.cocobeat.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.repository.FoodRepository

class FoodViewModelFactory constructor(private val repository: FoodRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FoodViewModel::class.java!!)) {
            FoodViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}