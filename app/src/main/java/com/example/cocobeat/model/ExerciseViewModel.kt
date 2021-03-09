package com.example.cocobeat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cocobeat.database.entity.Exercise
import com.example.cocobeat.repository.ExerciseRepository

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {
    var recentExercise: LiveData<List<Exercise>>? = null
    var frequentExercise: LiveData<List<Exercise>>? = null

    var exercises: LiveData<List<Exercise>>? = null

    fun insertExercise(exercise: Exercise){
        repository.insertExercise(exercise)
    }

    fun getRecentExercise() {
        recentExercise = repository.getRecentExercise()
    }

    fun getFrequentExercise(){
        frequentExercise = repository.getFrequentExercise()
    }

    fun insertExercises(exercises: MutableList<Exercise>){
        repository.insertExercises(exercises)
    }

    fun getAllExercises(){
        exercises = repository.getAllExercises()
    }
}