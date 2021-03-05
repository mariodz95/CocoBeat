package com.example.cocobeat.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.cocobeat.R
import com.example.cocobeat.databinding.ActivityCreateExerciseBinding
import com.example.cocobeat.databinding.ExerciseTitleBinding
import com.example.cocobeat.fragment.TimePickerDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class CreateExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateExerciseBinding
    var minute: Int = 0
     var hour: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val cal = Calendar.getInstance()
        binding.textTime.text = SimpleDateFormat("HH:mm").format(cal.time)

        binding.textTime.setOnClickListener{
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.textTime.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dateFormat = SimpleDateFormat("dd.MMM.YYYY")
        binding.textDate.text = dateFormat.format(cal.time)

        binding.textDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.textDate.text = dateFormat.format(cal.time)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        val doneBtn: Toolbar = findViewById(R.id.done)
        doneBtn.setOnClickListener{
            Log.v("test", "test")
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