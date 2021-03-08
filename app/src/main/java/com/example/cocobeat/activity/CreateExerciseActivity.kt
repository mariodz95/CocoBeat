package com.example.cocobeat.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.R
import com.example.cocobeat.database.entity.Exercise
import com.example.cocobeat.databinding.ActivityCreateExerciseBinding
import com.example.cocobeat.fragment.TimePickerDialogFragment
import com.example.cocobeat.model.ExerciseViewModel
import com.example.cocobeat.model.ExerciseViewModelFactory
import com.example.cocobeat.repository.ExerciseRepository
import com.example.cocobeat.view.DateAndTime
import com.example.cocobeat.view.MonthPicker
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class CreateExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateExerciseBinding
    var minute: Int = 0
    var hour: Int = 0
    lateinit var mDate: Date
    lateinit var mTime: String

    private val repository : ExerciseRepository by inject()
    private lateinit var exerciseViewModel: ExerciseViewModel

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var factory = ExerciseViewModelFactory(repository)
        exerciseViewModel = ViewModelProvider(this, factory)[ExerciseViewModel::class.java]

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.exercise_title)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.timeTextInput.setOnClickListener{
            showTimePickerDialog()
        }

        binding.dateAndTimeView.setDateAndTimeListener(object : DateAndTime.OnDateAndTImeChangeListener {
            override fun getDateAndTime(date: Date, time: String) {
                mDate = date
                mTime = time
            }
        })

        val doneBtn: Toolbar = findViewById(R.id.done)
        doneBtn.setOnClickListener{

            var name: String = binding.nameTextInput.text.toString()
            var exercise = Exercise(UUID.randomUUID(), name, hour, minute, mDate, mTime)
            if(name.isEmpty()){
                binding.nameTextInput.error = "Name is required!"
                binding.nameTextInput.hint = "Name duration is required"
                binding.nameTextInput.setHintTextColor(Color.parseColor("#FF0303"))
            }else if(hour == 0 && minute == 0)
            {
                binding.timeTextInput.error = "Time duration is required!"
                binding.timeTextInput.hint = "Time duration is required"
                binding.timeTextInput.setHintTextColor(Color.parseColor("#FF0303"))
            }else{
                exerciseViewModel.insertExercise(exercise)
                val intent = Intent(this, ExerciseActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showTimePickerDialog() {
        var dialog = TimePickerDialogFragment()

        dialog.setOnDoneListener(object : TimePickerDialogFragment.DoneListener {
            override fun onDone(hours: Int, minutes: Int) {
                hour = hours
                minute = minutes
                binding.timeTextInput.setText("$hours hour, $minutes minutes")
            }
        })

        dialog.show(supportFragmentManager, "deviceListDialogFragment")
    }
}