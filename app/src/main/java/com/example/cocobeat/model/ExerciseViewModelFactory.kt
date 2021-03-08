package com.example.cocobeat.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.repository.ExerciseRepository

class ExerciseViewModelFactory constructor(private val repository: ExerciseRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ExerciseViewModel::class.java!!)) {
            ExerciseViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}