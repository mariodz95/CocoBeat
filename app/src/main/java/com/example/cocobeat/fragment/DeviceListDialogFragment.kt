package com.example.cocobeat.fragment

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.R
import com.example.cocobeat.activity.DevicesActivity
import com.example.cocobeat.adapter.DevicesAdapter
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.databinding.ActivityDeviceDialogBinding
import com.example.cocobeat.model.DeviceViewModel
import com.example.cocobeat.model.ReadingViewModel
import com.example.cocobeat.repository.AccuCheckDevice
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class DeviceListDialogFragment : DialogFragment(), DevicesAdapter.OnItemClickListener {
    private val mReadingViewModel: ReadingViewModel by sharedViewModel()
    private val mDeviceViewModel: DeviceViewModel by sharedViewModel()

    private val deviceList = mutableListOf<Device>()
    private var _binding: ActivityDeviceDialogBinding? = null
    private val binding get() = _binding!!
    var activity: Activity? = getActivity()

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?
    ): View? {
        _binding = ActivityDeviceDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recyclerViewDialog.layoutManager = LinearLayoutManager(this.context)

        deviceList.add(
            Device(
                UUID.randomUUID(),
                "",
                null,
                "Movesense Wearable",
                R.drawable.ic_launcher_background
            )
        )

        deviceList.add(
            Device(
                UUID.randomUUID(),
                "",
                null,
                "Accu-Chek",
                R.drawable.ic_launcher_background
            )
        )

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

        val  filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        context?.registerReceiver(mBondingBroadcastReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(mBondingBroadcastReceiver);
    }

    override fun onItemClick(position: Int) {
        val clickedItem = deviceList[position]

        viewLifecycleOwner.lifecycleScope.launch{
            connectToDevice(clickedItem.name)
        }
    }

    private fun connectToDevice(deviceName: String?) {
        val accuCheckDevice = AccuCheckDevice()
        GlobalScope.launch {
            accuCheckDevice.scanForNearbyDevices(deviceName)
            accuCheckDevice.pairWithDevice(deviceName)
            accuCheckDevice.setOnSyncListener(object : AccuCheckDevice.SyncListener {
                override fun onSyncComplete(allReadings: MutableList<Reading>, device: Device) {
                    insertToDatabase(allReadings, device)
                }
            })
            accuCheckDevice.readCharacteristic(deviceName)
        }
    }

    private fun insertToDatabase(allReadings: MutableList<Reading>, device: Device) {
        mReadingViewModel.insertReadings(allReadings)
        mDeviceViewModel.insertDevice(device)
        dialog?.dismiss()
    }

    private val mBondingBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
        }
    }
}