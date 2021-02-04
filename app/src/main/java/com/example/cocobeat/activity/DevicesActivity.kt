package com.example.cocobeat.activity

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.ablelib.manager.AbleManager
import com.ablelib.util.AbleLogOptions
import com.ablelib.util.turnBluetoothOnIfOff
import com.example.cocobeat.model.DeviceDataModel
import com.example.cocobeat.R
import com.example.cocobeat.adapter.DevicesAdapter
import com.example.cocobeat.databinding.ActivityDevicesBinding
import com.example.cocobeat.fragment.DeviceListDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class DevicesActivity : AppCompatActivity(), CoroutineScope, DevicesAdapter.OnItemClickListener {
    private val deviceList = ArrayList<DeviceDataModel>()
    private var job: Job = Job()
    private lateinit var binding: ActivityDevicesBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (AbleManager.shared) {
            initialize(this@DevicesActivity)
            handlePermissionRequestIfNotGranted(this@DevicesActivity)
        }

        AbleManager.shared.loggingOptions = AbleLogOptions.Issues

        turnBluetoothOnIfOff()
        statusCheck()

        for(i in 0..5){
            deviceList.add(DeviceDataModel(R.drawable.ic_launcher_background, "Device 1", "xx/xx/XXXX"))
        }

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_device_list_title)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.recyclerView.apply{
                adapter = DevicesAdapter(deviceList, this@DevicesActivity)
                layoutManager = LinearLayoutManager(this@DevicesActivity)
        }

        binding.buttonAddDevice.setOnClickListener{
            openDeviceDialog()
        }
    }

    private fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, you need to enable it for device scanning")
                .setCancelable(false)
                .setPositiveButton("Yes"){_, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))}
                .setNegativeButton("No"){dialog, _ -> dialog.cancel()}
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun openDeviceDialog() {
        var dialog = DeviceListDialogFragment()
        dialog.show(supportFragmentManager, "deviceListDialogFragment")
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}

