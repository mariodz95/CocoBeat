package com.example.cocobeat

import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ablelib.manager.AbleManager
import com.ablelib.util.AbleLogOptions
import com.ablelib.util.turnBluetoothOnIfOff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class DevicesActivity : AppCompatActivity(), CoroutineScope, DevicesAdapter.OnItemClickListener {
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
        AbleManager.shared.loggingOptions = AbleLogOptions.Issues

        turnBluetoothOnIfOff()
        statusCheck()

        for(i in 0..5){
            deviceList.add(DeviceDataModel(R.drawable.ic_launcher_background, "Device 1", "xx/xx/XXXX"))
        }

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.toolbar_device_list_title);

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            // back button pressed
            onBackPressed()
        }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = DevicesAdapter(deviceList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, you need to enable it for device scanning")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
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

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}

