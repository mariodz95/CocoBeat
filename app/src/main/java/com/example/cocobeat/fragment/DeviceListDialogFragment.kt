package com.example.cocobeat.fragment

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.R
import com.example.cocobeat.adapter.DevicesDialogAdapter
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.databinding.ActivityDeviceDialogBinding
import kotlinx.coroutines.launch
import java.util.*


class DeviceListDialogFragment : DialogFragment(), DevicesDialogAdapter.OnItemClickListener {
    private val deviceList = mutableListOf<Device>()
    private var _binding: ActivityDeviceDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var dialogListener: DialogClosedListener

    fun setOnDialogCloseListener(listener: DialogClosedListener) {
        dialogListener = listener
    }

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

        binding.recyclerViewDialog.adapter = DevicesDialogAdapter(deviceList, this)

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
        dialog?.dismiss()
        dialogListener.onClose(deviceName)
    }

    private val mBondingBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
        }
    }

    interface DialogClosedListener{
        fun onClose(deviceName: String?)
    }
}