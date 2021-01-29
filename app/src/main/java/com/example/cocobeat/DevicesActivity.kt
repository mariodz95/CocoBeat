package com.example.cocobeat

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DevicesActivity : AppCompatActivity() {
    private val deviceList = ArrayList<DeviceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        for(i in 0..5){
            deviceList.add(  DeviceViewModel(R.drawable.ic_launcher_background,"Device 1", "xx/xx/XXXX"))
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

