package com.example.cocobeat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cocobeat.database.entity.Step
import com.example.cocobeat.repository.StepRepository
import java.util.*

class StepViewModel(private val repository: StepRepository) : ViewModel() {
    var lastStep: LiveData<Step> = repository.lastStep

    fun insertSteps(steps: MutableList<Step>){
        repository.insertSteps(steps)
    }

    fun updateStep(newSteps: Int, dateEnded: String, id: UUID){
        repository.updateStep(newSteps, dateEnded, id)
    }
}