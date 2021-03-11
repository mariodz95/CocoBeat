package com.example.cocobeat.repository

import androidx.lifecycle.LiveData
import com.example.cocobeat.database.dao.StepDao
import com.example.cocobeat.database.entity.Step
import java.util.*

class StepRepository(private val stepDao: StepDao){
    var lastStep: LiveData<Step> = stepDao.getLastStep()

    fun insertSteps(steps: MutableList<Step>){
        stepDao.insertSteps(steps)
    }

    fun updateStep(newSteps: Int, dateEnded: String, id: UUID) {
        stepDao.updateStep(newSteps, dateEnded, id)
    }
}