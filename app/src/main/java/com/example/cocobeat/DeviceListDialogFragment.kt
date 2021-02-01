package com.example.cocobeat

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ablelib.comm.asyncComm
import com.ablelib.comm.comm
import com.ablelib.exceptions.BluetoothStateException
import com.ablelib.manager.AbleManager
import com.ablelib.manager.pair
import com.ablelib.models.AbleDevice
import com.ablelib.models.AbleUUID
import com.ablelib.storage.AbleDeviceStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class DeviceListDialogFragment : DialogFragment(), DevicesAdapter.OnItemClickListener {
    private val deviceList = ArrayList<DeviceDataModel>()
    private lateinit var devicesAdapter: DevicesAdapter

    override  fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate (R.layout.activity_device_dialog, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_view_dialog)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        deviceList.add(  DeviceDataModel(R.drawable.ic_launcher_background,"Movesense Wearable", null))
        deviceList.add(  DeviceDataModel(R.drawable.ic_launcher_background,"Accu-Chek", null))

        recyclerView.adapter = DevicesAdapter(deviceList, this)

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

    override fun onItemClick(position: Int) {
        val clickedItem = deviceList[position]
        GlobalScope.async  {
            scanForNearbyDevices()
        }

    }

    suspend fun scanForNearbyDevices() {
        val value = GlobalScope.async {
            try {
                val devices = AbleManager.shared.scan()
                for (device in devices) {
                    AbleDeviceStorage.default.add(AbleDevice(device.name, true, true, true, device.address))
                }
            } catch (e: BluetoothStateException) {
                // handle the exception
            }
        }
        value.await()
        pairWithDevice()

        val myDevice = AbleDeviceStorage.default.findByName("Accu-Chek")
        Log.v("test", myDevice.toString())

        val comm = myDevice.asyncComm.onConnectionStateChanged { newState, status ->
                    // respond to connection state changes - connections, disconnects, etc
                     Log.v("test", status.toString())
                }
                .onServicesDiscovered { services ->
                    // here's where the services will appear once you trigger discoverServices
                    Log.v("test", "onServicesDiscovered $services")
                }
                .onCharacteristicRead { characteristic ->
                    // called after comm.readCharacteristic
                    Log.v("test", "                .onCharacteristicRead { characteristic ->\n")
                }
                .onCharacteristicChanged { characteristic ->
                    // called when a characteristic for which setNotifyValue was called changes its value
                    Log.v("test", "onCharacteristicChanged")
                }
                .onDescriptorRead { descriptor ->
                    // called after comm.readDescriptor
                    Log.v("test", "onDescriptorRead")
                }
                .onError { error ->
                    Log.v("test", "Error")

                }
                .connect()

    }

    suspend fun pairWithDevice() {
        val myDevice = AbleDeviceStorage.default.findByName("Accu-Chek")
        try {
            val pairedDevice = myDevice.pair()
        } catch (e: Exception) {
            // handle the exception
        }
    }
}