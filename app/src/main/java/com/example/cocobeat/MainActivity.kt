package com.example.cocobeat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val model: DateViewModel by viewModels()
    lateinit var textViewDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.toolbar_title_layout);

        textViewDate = findViewById(R.id.text_view_date)
        displayDate()
    }

    fun openDevicesActivity(view: View) {
        val intent = Intent(this, DevicesActivity::class.java)
        startActivity(intent)
    }

    private fun displayDate(){
        var monthDisplay = model.getMonth()
        var yearDisplay = model.getYear()
        textViewDate.text = ("$monthDisplay $yearDisplay")
    }


    fun getNextMonth(view: View){
        var newMonthDate = model.getNextMonth()
        var newMonthYear = model.getYear()
        textViewDate.text = ("$newMonthDate $newMonthYear")
    }

    fun getPrevMonth(view: View){
        var newMonthDate = model.getPrevMonth()
        var newMonthYear = model.getYear()
        textViewDate.text = ("$newMonthDate $newMonthYear")
    }
}