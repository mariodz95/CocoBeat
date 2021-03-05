package com.example.cocobeat.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.R
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.databinding.ActivityDeviceDialogBinding
import com.example.cocobeat.databinding.TimePickerDialogLayoutBinding
import com.example.cocobeat.repository.AccuCheckDevice
import java.util.*
import kotlin.math.min


class TimePickerDialogFragment : DialogFragment() {

    var minutes: Int = 0
    var hours : Int = 0

    private lateinit var doneListener: TimePickerDialogFragment.DoneListener

    fun setOnDoneListener(listener: TimePickerDialogFragment.DoneListener) {
        doneListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.time_picker_dialog_layout, null)
            builder.setView(dialogView)

            val timePicker: TimePicker = dialogView.findViewById(R.id.time_picker)
            timePicker.setIs24HourView(true)

            timePicker.minute = minutes
            timePicker.hour = hours


            timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                minutes = minute
                hours = hourOfDay
            }

            builder.setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        doneListener.onDone(hours, minutes)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface DoneListener{
        fun onDone(hours: Int, minutes: Int)
    }
}