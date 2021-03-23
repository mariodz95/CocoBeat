package com.example.cocobeat.repository

import com.example.cocobeat.database.dao.FoodDao
import com.example.cocobeat.database.entity.Food

class FoodRepository(private val foodDao: FoodDao)
{

    fun insertFood(food: Food){
        foodDao.insertFood(food)
    }
}