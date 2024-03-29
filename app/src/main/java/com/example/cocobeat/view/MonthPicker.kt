package com.example.cocobeat.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.cocobeat.databinding.DatePickerLayoutBinding
import java.text.DateFormatSymbols
import java.util.*

class MonthPicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes){
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var binding: DatePickerLayoutBinding = DatePickerLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    private var monthNumber = calendar.get(Calendar.MONTH)
    private var monthName = getMonthForInt(monthNumber)

    lateinit var onMonthChange: OnMonthChangeListener

    init {
        displayDate()
        binding.imgArrowBack.setOnClickListener{
            getPrevMonth()
        }
        binding.imgArrowNext.setOnClickListener{
            getNextMonth()
        }
        orientation = VERTICAL
    }

    fun setMonthAndYear(month: Int, year: Int) {
        monthNumber = month
        monthName =  getMonthForInt(monthNumber)
        this.year = year
        displayDate()
        onMonthChange.getMonthAndYear(monthNumber, year)
    }

    fun setOnMonthListener(listener: OnMonthChangeListener) {
        onMonthChange = listener
        onMonthChange.getMonthAndYear(monthNumber, year)
    }

     fun getMonthForInt(num: Int): String? {
        var month = "error"
        val dfs = DateFormatSymbols()
        val months: Array<String> = dfs.months
        if (num in 0..11) {
            month = months[num]
        }
        return month
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("year", year)
        bundle.putString("monthName", monthName)
        bundle.putInt("monthNumber", monthNumber)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle)
        {
            val bundle = state
            year = bundle.getInt("year")
            monthName = bundle.getString("monthName")
            monthNumber = bundle.getInt("monthNumber")
            state = bundle.getParcelable("superState")
            displayDate()
        }
        super.onRestoreInstanceState(state)
    }

    private fun displayDate(){
        binding.textViewDate.text = ("$monthName $year")
    }

    private fun getNextMonth() {
        calendar.add(Calendar.MONTH, 1)
        monthNumber += 1
        if(monthNumber > 11)
        {
            year += 1
            monthNumber = 0
        }
        monthName = getMonthForInt(monthNumber)
        displayDate()

        onMonthChange.getMonthAndYear(monthNumber, year)
    }

    private fun getPrevMonth(){
        calendar.add(Calendar.MONTH, -1)
        monthNumber -= 1
        if(monthNumber < 0)
        {
            year -= 1
            monthNumber = 11
        }
        monthName = getMonthForInt(monthNumber)
        displayDate()

        onMonthChange.getMonthAndYear(monthNumber, year)
    }

    interface OnMonthChangeListener{
        fun getMonthAndYear(monthNumber: Int, year: Int)
    }
}
