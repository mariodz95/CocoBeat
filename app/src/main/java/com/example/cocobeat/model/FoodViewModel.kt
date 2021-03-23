package com.example.cocobeat.model

import androidx.lifecycle.ViewModel
import com.example.cocobeat.database.entity.Food
import com.example.cocobeat.repository.FoodRepository

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {
    fun insertFood(food: Food){
        repository.insertFood(food)
    }
}