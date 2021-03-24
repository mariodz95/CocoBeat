package com.example.cocobeat.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.R
import com.example.cocobeat.adapter.HistoryAdapter
import com.example.cocobeat.databinding.ActivityHistoryBinding
import com.example.cocobeat.model.*
import com.example.cocobeat.repository.ExerciseRepository
import com.example.cocobeat.repository.FoodRepository
import com.example.cocobeat.repository.ReadingRepository
import com.example.cocobeat.repository.StepRepository
import com.example.cocobeat.util.HistoryItemType
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    private lateinit var readingViewModel: ReadingViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var stepViewModel: StepViewModel
    private lateinit var foodViewModel: FoodViewModel

    private val readingRepository : ReadingRepository by inject()
    private val exerciseRepository : ExerciseRepository by inject()
    private val stepRepository : StepRepository by inject()
    private val foodRepository : FoodRepository by inject()

    var historyItems: MutableList<HistoryItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_title_layout)
        }

        var readingFactory = ReadingViewModelFactory(readingRepository)
        readingViewModel = ViewModelProvider(this, readingFactory)[ReadingViewModel::class.java]

        var exerciseFactory = ExerciseViewModelFactory(exerciseRepository)
        exerciseViewModel = ViewModelProvider(this, exerciseFactory)[ExerciseViewModel::class.java]

        var stepFactory = StepViewModelFactory(stepRepository)
        stepViewModel = ViewModelProvider(this, stepFactory)[StepViewModel::class.java]

        var foodFactory = FoodViewModelFactory(foodRepository)
        foodViewModel = ViewModelProvider(this, foodFactory)[FoodViewModel::class.java]

        readingViewModel.getReadings()
        exerciseViewModel.getAllExercises()

        readingViewModel.readings?.observe(this@HistoryActivity, androidx.lifecycle.Observer {
            if (it.isNotEmpty()) {
                for (reading in it) {
                    val item: HistoryItem = HistoryItem(
                        HistoryItemType.READING,
                        reading.dateAdded,
                        reading.value.toString(),
                        reading.units
                    )
                    historyItems?.add(item)
                }
            }
            populateAdapter()
        })

        exerciseViewModel.exercises?.observe(this@HistoryActivity, androidx.lifecycle.Observer {
            if (it.isNotEmpty()) {
                for (exercise in it) {
                    val item: HistoryItem = HistoryItem(
                        HistoryItemType.EXERCISE,
                        exercise.dateAdded,
                        "${exercise.exerciseName} ${exercise.hourDuration!! * 60 + exercise.minuteDuration!!}",
                        "minutes"
                    )
                    historyItems?.add(item)
                }
            }
            populateAdapter()
        })

        stepViewModel.allStepData?.observe(this, androidx.lifecycle.Observer {
            val format = SimpleDateFormat("yyyy-MM-dd")
            if (it.isNotEmpty()) {
                for (step in it) {
                    val item: HistoryItem = HistoryItem(
                        HistoryItemType.STEP,
                        format.parse(step.dateEnded),
                        "${step.steps}",
                        null
                    )
                    historyItems?.add(item)
                }
                populateAdapter()
            }
        })

        foodViewModel.allFood?.observe(this, androidx.lifecycle.Observer {
            if(it.isNotEmpty()){
                for(food in it){
                    val item: HistoryItem = HistoryItem(HistoryItemType.FOOD, food.dateAdded, "${food.foodName} ${food.calories}", null)
                    historyItems?.add(item)
                }
                populateAdapter()
            }
        })

    }

    private fun populateAdapter()
    {
        val orderedArray: List<HistoryItem> =
            historyItems.sortedByDescending { item -> item.date }

        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = HistoryAdapter(orderedArray)
        }
    }
}