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
import com.example.cocobeat.repository.ReadingRepository
import com.example.cocobeat.util.HistoryItemType
import org.koin.android.ext.android.inject
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    private lateinit var readingViewModel: ReadingViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel

    private val readingRepository : ReadingRepository by inject()
    private val exerciseRepository : ExerciseRepository by inject()

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

        readingViewModel.getReadings()
        exerciseViewModel.getAllExercises()

        readingViewModel.readings?.observe(this@HistoryActivity, androidx.lifecycle.Observer {
            for (reading in it) {
                val item: HistoryItem = HistoryItem(
                    HistoryItemType.READING,
                    reading.dateAdded,
                    reading.value.toString(),
                    reading.units
                )
                historyItems?.add(item)
            }

            val orderedArray: List<HistoryItem> =
                historyItems.sortedByDescending { item -> item.date }

            binding.historyRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                adapter = HistoryAdapter(orderedArray)
            }
        })

        exerciseViewModel.exercises?.observe(this@HistoryActivity, androidx.lifecycle.Observer {
            for (exercise in it) {
                val item: HistoryItem = HistoryItem(
                    HistoryItemType.EXERCISE,
                    exercise.dateAdded,
                    "${exercise.exerciseName} ${exercise.hourDuration!! * 60 + exercise.minuteDuration!!}",
                    "minutes"
                )
                historyItems?.add(item)
            }
        })
    }
}