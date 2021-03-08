package com.example.cocobeat.repository

import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.ExerciseDao
import com.example.cocobeat.database.entity.Exercise


class ExerciseRepository(private val exerciseDao: ExerciseDao) {
    private var recentExercise: LiveData<List<Exercise>>? = null
    private var frequentExercise: LiveData<List<Exercise>>? = null

    fun insertExercise(exercise: Exercise){
        exerciseDao.insertExercise(exercise)
    }

    fun getRecentExercise() : LiveData<List<Exercise>>? {
        recentExercise = exerciseDao.getRecentExercise()
        return recentExercise
    }

    fun getFrequentExercise() : LiveData<List<Exercise>>?{
        frequentExercise = exerciseDao.getFrequentExercise()
        return frequentExercise
    }

    fun insertExercises(exercises: MutableList<Exercise>){
        exerciseDao.insertExercises(exercises)
    }
}