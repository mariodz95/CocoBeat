package com.example.cocobeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DeviceListDialogFragment : DialogFragment() {
    private val deviceList = ArrayList<DeviceViewModel>()
    private lateinit var devicesAdapter: DevicesAdapter

    override  fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate (R.layout.activity_device_dialog, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_view_dialog)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        for(i in 0..1){
            deviceList.add(  DeviceViewModel(R.drawable.ic_launcher_background,"Device dialog", null))
        }
        recyclerView.adapter = DevicesAdapter(deviceList)

        val button: Button = rootView.findViewById(R.id.button_cancel)

        button.setOnClickListener{
            dialog?.dismiss()
        }

        return rootView
    }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}