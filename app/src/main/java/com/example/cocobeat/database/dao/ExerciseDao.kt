package com.example.cocobeat.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cocobeat.database.entity.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise GROUP BY exercise_name, hour_duration, minute_duration")
    fun getRecentExercise(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise GROUP BY hour_duration, minute_duration, exercise_name HAVING COUNT(*) >= 5 ")
    fun getFrequentExercise(): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercises(exercise: MutableList<Exercise>)

    @Query("SELECT * FROM exercise ORDER BY date_added DESC")
    fun getAllExercises() : LiveData<List<Exercise>>
}