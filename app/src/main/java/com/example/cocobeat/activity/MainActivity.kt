package com.example.cocobeat.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.R
import com.example.cocobeat.databinding.ActivityMainBinding
import com.example.cocobeat.model.ReadingViewModel
import com.example.cocobeat.model.ReadingViewModelFactory
import com.example.cocobeat.repository.ReadingRepository
import com.example.cocobeat.util.Animation
import com.example.cocobeat.util.Helper
import com.example.cocobeat.view.MonthPicker
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private val repository : ReadingRepository by inject()
    private lateinit var readingViewModel: ReadingViewModel
    var dataExist: Boolean = false
    var isRotate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_title_layout)
        }

        val animation = Animation()
        animation.init(binding.fabDevices)
        animation.init(binding.fabExercise)

        binding.fab.setOnClickListener(View.OnClickListener { v ->
            isRotate = animation.rotateFab(v, !isRotate)
            if (isRotate) {
                animation.showIn(binding.fabExercise)
                animation.showIn(binding.fabDevices)
            } else {
                animation.showOut(binding.fabExercise)
                animation.showOut(binding.fabDevices)
            }
        })

        binding.fabDevices.setOnClickListener(View.OnClickListener {
            openDevicesActivity()
        })

        binding.fabExercise.setOnClickListener(View.OnClickListener {
            openExerciseActivity()
        })

        binding.btnHistory.setOnClickListener {
            openHistoryActivity()
        }

        var factory = ReadingViewModelFactory(repository)
        readingViewModel = ViewModelProvider(this, factory)[ReadingViewModel::class.java]

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
}