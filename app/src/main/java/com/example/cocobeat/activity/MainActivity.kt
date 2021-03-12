package com.example.cocobeat.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.R
import com.example.cocobeat.database.entity.Step
import com.example.cocobeat.databinding.ActivityMainBinding
import com.example.cocobeat.model.ReadingViewModel
import com.example.cocobeat.model.ReadingViewModelFactory
import com.example.cocobeat.model.StepViewModel
import com.example.cocobeat.model.StepViewModelFactory
import com.example.cocobeat.repository.ReadingRepository
import com.example.cocobeat.repository.StepRepository
import com.example.cocobeat.util.Animation
import com.example.cocobeat.util.Helper
import com.example.cocobeat.view.MonthPicker
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.HistoryClient
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private val repository : ReadingRepository by inject()
    private lateinit var readingViewModel: ReadingViewModel

    private val stepRepository : StepRepository by inject()
    private lateinit var stepViewModel: StepViewModel
    var dataExist: Boolean = false
    var isRotate: Boolean = false
    var connect: Boolean = true

    private lateinit var fitnessOptions: FitnessOptions
    private val stepList: MutableList<Step> = mutableListOf()
    private var lastStep: Step? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var factory = ReadingViewModelFactory(repository)
        readingViewModel = ViewModelProvider(this, factory)[ReadingViewModel::class.java]

        var stepFactory = StepViewModelFactory(stepRepository)
        stepViewModel = ViewModelProvider(this, stepFactory)[StepViewModel::class.java]

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_title_layout)
        }


        stepViewModel.lastStep.observe(this, androidx.lifecycle.Observer {
            lastStep = it
            if (connect) {
                checkIsItConnected()
            }
        })

        val animation = Animation()
        animation.init(binding.fabDevices)
        animation.init(binding.fabExercise)
        animation.init(binding.fabFitData)

        binding.fab.setOnClickListener(View.OnClickListener { v ->
            isRotate = animation.rotateFab(v, !isRotate)
            if (isRotate) {
                animation.showIn(binding.fabExercise)
                animation.showIn(binding.fabDevices)
                animation.showIn(binding.fabFitData)
            } else {
                animation.showOut(binding.fabExercise)
                animation.showOut(binding.fabDevices)
                animation.showOut(binding.fabFitData)
            }
        })

        binding.fabDevices.setOnClickListener {
            openDevicesActivity()
        }

        binding.fabExercise.setOnClickListener {
            openExerciseActivity()
        }

        binding.fabFitData.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    1
                )
            }
            connectToGoogleFitApi()
        }

        binding.btnHistory.setOnClickListener {
            openHistoryActivity()
        }

        var entries = ArrayList<Entry>()

        binding.monthPickerView.setOnMonthListener(object : MonthPicker.OnMonthChangeListener {
            override fun getMonthAndYear(monthNumber: Int, year: Int) {
                var helper = Helper()
                val startDate = helper.getDate(year, monthNumber, 0, 0, 0, true)
                val endDate = helper.getDate(year, monthNumber, 0, 0, 0, false)

                endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))

                readingViewModel.loadMonthData(startDate.time, endDate.time)

                readingViewModel.monthData?.observe(this@MainActivity, androidx.lifecycle.Observer {
                    entries = arrayListOf<Entry>()
                    binding.chart.data = null
                    binding.chart.notifyDataSetChanged()
                    binding.chart.invalidate()

                    if (it.isNotEmpty()) {
                        val grouped =
                            it.groupBy { DateFormat.format("dd", it.readingDate) }.mapValues {
                                helper.calculateAverage(
                                    it.value.map { it.value })
                            }
                        drawLineGraph(grouped, entries)
                        dataExist = true
                    } else if (it.isEmpty() && !dataExist) {
                        readingViewModel.lastReading.observe(
                            this@MainActivity,
                            androidx.lifecycle.Observer {
                                if (it !== null) {
                                    var year: Int =
                                        DateFormat.format("yyyy", it.readingDate).toString().toInt()
                                    var month: Int =
                                        DateFormat.format("M", it.readingDate).toString().toInt()
                                    binding.monthPickerView.setMonthAndYear(month, year)
                                }
                            })
                    }
                })
            }
        })
    }



    private fun drawLineGraph(grouped: Map<CharSequence, Double>, entries: ArrayList<Entry>){
        for ((key, value) in grouped) {
            entries.add(Entry(key.toString().toFloat(), value.toFloat()))
        }

        val dataSet = LineDataSet(entries, "")
        dataSet.color = resources.getColor(R.color.green)
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.lineWidth = 3f

        val legendEntry = LegendEntry()
        legendEntry.label = "Avg. mg/dL"
        legendEntry.formColor = Color.GREEN

        val legend: Legend = binding.chart.legend
        legend.setCustom(listOf(legendEntry))
        legend.form = Legend.LegendForm.CIRCLE

        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

        val lineData = LineData(dataSet)
        binding.chart.description.isEnabled = false
        binding.chart.axisRight.isEnabled = false
        binding.chart.axisLeft.setDrawGridLines(false)
        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.setTouchEnabled(false)
        binding.chart.setPinchZoom(false)
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.data = lineData
        binding.chart.notifyDataSetChanged()
        binding.chart.invalidate()
    }

    private fun openDevicesActivity() {
        val intent = Intent(this, DevicesActivity::class.java)
        startActivity(intent)
    }

    private fun openExerciseActivity() {
        val intent = Intent(this, ExerciseActivity::class.java)
        startActivity(intent)
    }

    private fun openHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                1 -> accessGoogleFit()
                else -> {
                    // Result wasn't from Google Fit
                    Log.v("test", "Result wasn't from Google Fit\n")
                }
            }
            else -> {
                // Permission not granted
                Log.v("test", " // Permission not granted\n")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkIsItConnected()
    {
        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            accessGoogleFit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectToGoogleFitApi()
    {
        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                1,
                account,
                fitnessOptions
            )
        } else {
            accessGoogleFit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun accessGoogleFit() {
        var end = LocalDateTime.now()
        var start = end.minusYears(1)
        if(lastStep != null){
            start = LocalDateTime.parse(lastStep!!.dateEnded)
        }

        val endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond()
        val startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond()

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startSeconds, endSeconds, TimeUnit.SECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        Fitness.getHistoryClient(this, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // Use response data here
                // The aggregate query puts datasets into buckets, so flatten into a single list of datasets
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    dumpDataSet(dataSet)
                }
            }
            .addOnFailureListener { e ->
                Log.v(
                    "test", "There was an error reading data from Google Fit $e"
                )
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dumpDataSet(dataSet: DataSet) {
        val c = Calendar.getInstance()
        for (dp in dataSet.dataPoints) {
            for (field in dp.dataType.fields) {
                val step = Step(
                    UUID.randomUUID(),
                    dp.getValue(field).toString().toInt(),
                    c.time,
                    dp.getStartTimeString(),
                    dp.getEndTimeString()
                )
                if (lastStep == null) {
                    stepList.add(step)
                } else {
                    val dateOne = LocalDateTime.parse(lastStep?.dateEnded).toLocalDate()
                    val dateTwo = LocalDateTime.parse(step?.dateEnded).toLocalDate()
                    if (dateOne == dateTwo && step.steps != 0) {
                        val newSteps: Int = step.steps!! + lastStep!!.steps!!
                        stepViewModel.updateStep(newSteps, step.dateEnded.toString(), lastStep!!.id)
                    } else if(dateOne != dateTwo){
                        stepList.add(step)
                    }
                }
            }
        }
        if(!stepList.isNullOrEmpty()){
            connect = false
            stepViewModel.insertSteps(stepList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun DataPoint.getStartTimeString() = Instant.ofEpochSecond(this.getStartTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    fun DataPoint.getEndTimeString() = Instant.ofEpochSecond(this.getEndTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()
}