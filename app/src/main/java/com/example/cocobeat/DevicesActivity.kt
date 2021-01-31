package com.example.cocobeat

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ablelib.exceptions.BluetoothStateException
import com.ablelib.manager.AbleManager
import com.ablelib.manager.pair
import com.ablelib.models.AbleDevice
import com.ablelib.storage.AbleDeviceStorage
import com.ablelib.util.turnBluetoothOnIfOff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class DevicesActivity : AppCompatActivity(), CoroutineScope {
    private val deviceList = ArrayList<DeviceDataModel>()
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        AbleManager.shared.initialize(this)
        AbleManager.shared.handlePermissionRequestIfNotGranted(this)
        turnBluetoothOnIfOff()
        val devices: Set<AbleDevice> = AbleDeviceStorage.default.devices

        AbleManager.shared.scan { result ->
            result.onSuccess {
                // handle the devices set
                Log.v("find", devices.toString())
                Log.v("test", AbleDevice.toString())
            }.onFailure {
                // an exception has occurred
            }
        }

     /*   launch {
            scanForNearbyDevices()
        }*/


        for(i in 0..5){
            deviceList.add(  DeviceDataModel(R.drawable.ic_launcher_background,"Device 1", "xx/xx/XXXX"))
        }

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.toolbar_device_list_title);

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            // back button pressed
            onBackPressed()
        }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = DevicesAdapter(deviceList)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    suspend fun scanForNearbyDevices() {
        try {
            val devices = AbleManager.shared.scan()
            Log.v("test", devices.toString())
        } catch (e: BluetoothStateException) {
            // handle the exception
        }
    }

    suspend fun pairWithDevice() {
        val myDevice = AbleDeviceStorage.default.findByName("my awesome device")
        Log.v("kuca", myDevice.toString())

        try {
            val pairedDevice = myDevice.pair()
        } catch (e: Exception) {
            // handle the exception
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun openDeviceDialog(view: View) {
        val addDevice: Button = findViewById(R.id.button_add_device)
        addDevice.setOnClickListener{
            var dialog = DeviceListDialogFragment()

            dialog.show(supportFragmentManager, "deviceListDialogFragment")
        }
    }
}

