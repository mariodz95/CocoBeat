package com.example.cocobeat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.R
import com.example.cocobeat.adapter.DevicesAdapter
import com.example.cocobeat.databinding.ActivityDeviceDialogBinding
import com.example.cocobeat.model.DeviceDataModel
import com.example.cocobeat.repository.BluetoothService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.collections.ArrayList


class DeviceListDialogFragment : DialogFragment(), DevicesAdapter.OnItemClickListener {
    private val deviceList = ArrayList<DeviceDataModel>()
    private var _binding: ActivityDeviceDialogBinding? = null
    private val binding get() = _binding!!


    override  fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?
    ): View? {
        _binding = ActivityDeviceDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recyclerViewDialog.layoutManager = LinearLayoutManager(this.context)

        deviceList.add(
                DeviceDataModel(
                        R.drawable.ic_launcher_background,
                        "Movesense Wearable",
                        null
                )
        )
        deviceList.add(DeviceDataModel(R.drawable.ic_launcher_background, "Accu-Chek", null))

        binding.recyclerViewDialog.adapter = DevicesAdapter(deviceList, this)

        binding.buttonCancel.setOnClickListener{
            dialog?.dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onItemClick(position: Int) {
        val clickedItem = deviceList[position]
        GlobalScope.async  {
            connectToDevice(clickedItem.deviceName)
        }
    }

    private suspend fun connectToDevice(deviceName: String) {
        val bluetoothService = BluetoothService()
        val value = GlobalScope.async {
            bluetoothService.scanForNearbyDevices(deviceName)
        }
        value.await()
        bluetoothService.pairWithDevice(deviceName)
        bluetoothService.readCharacteristic(deviceName)
    }
}