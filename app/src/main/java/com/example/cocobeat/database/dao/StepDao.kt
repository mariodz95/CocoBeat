package com.example.cocobeat.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cocobeat.database.entity.Step
import java.util.*

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSteps(steps: MutableList<Step>)

    @Query("SELECT * FROM step ORDER BY date_ended DESC LIMIT 1")
    fun getLastStep() : LiveData<Step>

    @Query("UPDATE step SET steps = :newSteps, date_ended = :dateEnded WHERE id = :id")
    fun updateStep(newSteps: Int, dateEnded: String, id: UUID)
}