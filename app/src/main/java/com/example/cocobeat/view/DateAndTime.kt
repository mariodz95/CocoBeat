package com.example.cocobeat.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cocobeat.databinding.DateAndTimeLayoutBinding
import com.example.cocobeat.databinding.DatePickerLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class DateAndTime  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes){

    private var binding: DateAndTimeLayoutBinding = DateAndTimeLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    private val cal = Calendar.getInstance()
    var time: String = returnTime(cal.time)
    var date: Date = cal.time


    lateinit var onDateAndTimeChange: DateAndTime.OnDateAndTImeChangeListener

    fun setDateAndTimeListener(listener: DateAndTime.OnDateAndTImeChangeListener) {
        onDateAndTimeChange = listener
        onDateAndTimeChange.getDateAndTime(date, time)
    }

    init {
        binding.textTime.text = returnTime(cal.time)
        time = returnTime(cal.time)

        binding.textTime.setOnClickListener{
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time = returnTime(cal.time)
                binding.textTime.text = returnTime(cal.time)
                onDateAndTimeChange.getDateAndTime(date, time)
            }
            TimePickerDialog(
                getContext(),
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
                getContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = cal.time
                    binding.textDate.text = dateFormat.format(cal.time)
                    onDateAndTimeChange.getDateAndTime(date, time)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun returnTime(calendar: Date?) : String
    {
        return  SimpleDateFormat("HH:mm").format(calendar?.time)
    }


    interface OnDateAndTImeChangeListener{
        fun getDateAndTime(date: Date, time: String)
    }
}