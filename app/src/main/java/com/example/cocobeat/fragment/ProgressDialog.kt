package com.example.cocobeat.fragment

import android.R
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.databinding.ProgressDialogLayoutBinding
import com.example.cocobeat.model.DeviceViewModel
import com.example.cocobeat.model.ReadingViewModel
import com.example.cocobeat.repository.AccuCheckDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ProgressDialog(private val deviceName: String?) : DialogFragment(){

    private val mReadingViewModel: ReadingViewModel by sharedViewModel()
    private val mDeviceViewModel: DeviceViewModel by sharedViewModel()

    private var _binding: ProgressDialogLayoutBinding? = null
    private val binding get() = _binding!!

    var accuCheckDevice = AccuCheckDevice()

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?
    ): View? {
        _binding = ProgressDialogLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.progressBar.progress = 0
        binding.progressBar.isIndeterminate = false

        binding.textViewProgress.text = "Syncing with device"
        accuCheckDevice.setProgressListener(object : AccuCheckDevice.ProgressListener {
            override fun onProgress(progress: Int, numberOfReadings: Int) {
                binding.progressBar.max = numberOfReadings
                binding.progressBar.progress = progress
            }
        })

        connectToDevice()
        return view
    }

    private fun connectToDevice() {

        GlobalScope.launch(Dispatchers.Main){
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
        mDeviceViewModel.insertDevice(device)
        mReadingViewModel.insertReadings(allReadings)
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.progressBar.progress = 0
        _binding = null
    }

}