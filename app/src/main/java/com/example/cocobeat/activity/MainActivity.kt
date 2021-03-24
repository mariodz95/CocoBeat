package com.example.cocobeat.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.R
import com.example.cocobeat.database.entity.Step
import com.example.cocobeat.databinding.ActivityMainBinding
import com.example.cocobeat.model.*
import com.example.cocobeat.repository.OpenWeatherRepository
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
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

private const val API_KEY = "e75432998d51f673625612657db79aa1"

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private val repository : ReadingRepository by inject()
    private lateinit var readingViewModel: ReadingViewModel

    private val stepRepository : StepRepository by inject()
    private lateinit var stepViewModel: StepViewModel

    private val openWeatherRepository : OpenWeatherRepository by inject()
    private lateinit var mainActivityViewModel: MainActivityViewModel

    var dataExist: Boolean = false
    var isRotate: Boolean = false
    var connect: Boolean = true

    private lateinit var fitnessOptions: FitnessOptions
    private val stepList: MutableList<Step> = mutableListOf()
    private var lastStep: Step? = null
    var sum = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var factory = ReadingViewModelFactory(repository)
        readingViewModel = ViewModelProvider(this, factory)[ReadingViewModel::class.java]

        var stepFactory = StepViewModelFactory(stepRepository)
        stepViewModel = ViewModelProvider(this, stepFactory)[StepViewModel::class.java]

        var openWeatherFactory = MainActivityViewModelFactory(openWeatherRepository)
        mainActivityViewModel = ViewModelProvider(this, openWeatherFactory)[MainActivityViewModel::class.java]

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location != null){
                    val url: String = ("/data/2.5/weather?lat=${location?.latitude}&lon=${location?.longitude}&appid=$API_KEY")
                    mainActivityViewModel.getTemperature(url)
                }
            }

        mainActivityViewModel.temperature.observe(this, androidx.lifecycle.Observer {
            if (it.isSuccessful) {
                val decimal = DecimalFormat("0.00")
                val temperature: Double = it.body()?.main?.temp!! - 273.15
                binding.temperature.text = ("${decimal.format((it.body()?.main?.temp!! - 273.15))} °C")

                if(temperature > 7 && temperature < 20) {
                    val toast = Toast.makeText(applicationContext, "Good time for running!",  Toast.LENGTH_LONG)
                    toast.show()
                }
            }
        })

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_title_layout)
        }

        val prefs = getSharedPreferences("SETTINGS", MODE_PRIVATE)
        var monthSteps = prefs.getInt("monthSteps", 0)

        if(monthSteps == 0){
            val editor = getSharedPreferences("SETTINGS", MODE_PRIVATE).edit()
            monthSteps = 150000
            editor.putInt("monthSteps", 150000)
            editor.apply()
        }

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        var startDate: Date= calendar.time
        val sdf = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val startDateString = sdf.format(startDate).toString()
        val endDateString = sdf.format(calendar.time).toString()

        stepViewModel.getMonthData(startDateString, endDateString)
        stepViewModel.monthSteps?.observe(this, androidx.lifecycle.Observer {
            sum = 0
            for (step in it) {
                sum += step.steps!!
            }
            binding.progressBar.progress = sum
            binding.currentProgress.text = "$sum/$monthSteps steps"
            binding.progressBar.max = monthSteps
        })

        binding.currentProgress.text = "$sum/$monthSteps steps"
        binding.progressBar.max = monthSteps

        binding.settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
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
        animation.init(binding.fabFood)

        binding.fab.setOnClickListener(View.OnClickListener { v ->
            isRotate = animation.rotateFab(v, !isRotate)
            if (isRotate) {
                animation.showIn(binding.fabExercise)
                animation.showIn(binding.fabDevices)
                animation.showIn(binding.fabFitData)
                animation.showIn(binding.fabFood)
            } else {
                animation.showOut(binding.fabExercise)
                animation.showOut(binding.fabDevices)
                animation.showOut(binding.fabFitData)
                animation.showOut(binding.fabFood)
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

        binding.fabFood.setOnClickListener{
            openFoodFormActivity()
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

    private fun openFoodFormActivity(){
        val intent = Intent(this, FoodFormActivity::class.java)
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