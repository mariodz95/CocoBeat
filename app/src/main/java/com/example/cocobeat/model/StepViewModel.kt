package com.example.cocobeat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cocobeat.database.entity.Step
import com.example.cocobeat.repository.StepRepository
import java.util.*

class StepViewModel(private val repository: StepRepository) : ViewModel() {
    var lastStep: LiveData<Step> = repository.lastStep
    var monthSteps: LiveData<List<Step>>? = null
    var allStepData: LiveData<List<Step>>? = repository.allStepData

    fun insertSteps(steps: MutableList<Step>){
        repository.insertSteps(steps)
    }

    fun updateStep(newSteps: Int, dateEnded: String, id: UUID){
        repository.updateStep(newSteps, dateEnded, id)
    }

    fun getMonthData(dateStarted: String, dateEnded: String){
        monthSteps = repository.getMonthData(dateStarted, dateEnded)
    }
}