package com.example.cocobeat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month_date = SimpleDateFormat("MMMM")
    var month_name = month_date.format(c.getTime())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.toolbar_title_layout);

        displayDate()
    }

    private fun displayDate(){
        val textViewDate: TextView = findViewById(R.id.text_view_date) as TextView
        textViewDate.text = ("$month_name $year")
    }

    fun openDevicesActivity(view: View) {
        val intent = Intent(this, DevicesActivity::class.java )
        startActivity(intent)
    }

    fun getNextMonth(view: View){
        val textViewDate: TextView = findViewById(R.id.text_view_date) as TextView

        c.add(Calendar.MONTH, 1);
        val thisMonth: Int = c.get(Calendar.MONTH)
        if(thisMonth == 0)
        {
            year = c.get(Calendar.YEAR)
        }

        month_name = month_date.format(c.getTime())
        textViewDate.text = ("$month_name $year")
    }

    fun getPrevMonth(view: View){
        val textViewDate: TextView = findViewById(R.id.text_view_date) as TextView

        c.add(Calendar.MONTH, -1);
        val thisMonth: Int = c.get(Calendar.MONTH)
        if(thisMonth == 11)
        {
            //c.add(Calendar.YEAR, -1)
            year = c.get(Calendar.YEAR)
        }
        month_name = month_date.format(c.getTime())
        textViewDate.text = ("$month_name $year")
    }
}