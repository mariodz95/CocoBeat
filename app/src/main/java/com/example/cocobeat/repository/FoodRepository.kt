package com.example.cocobeat.repository

import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.FoodDao
import com.example.cocobeat.database.entity.Food

class FoodRepository(private val foodDao: FoodDao)
{
    var allFood: LiveData<List<Food>>? = foodDao.getAllFood()

    fun insertFood(food: Food){
        foodDao.insertFood(food)
    }
}