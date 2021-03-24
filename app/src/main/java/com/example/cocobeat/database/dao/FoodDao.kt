package com.example.cocobeat.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cocobeat.database.entity.Food

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: Food)

    @Query("SELECT * FROM food")
    fun getAllFood() : LiveData<List<Food>>
}