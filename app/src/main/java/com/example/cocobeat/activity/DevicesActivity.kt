package com.example.cocobeat.activity

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ablelib.util.turnBluetoothOnIfOff
import com.example.cocobeat.R
import com.example.cocobeat.adapter.DevicesAdapter
import com.example.cocobeat.databinding.ActivityDevicesBinding
import com.example.cocobeat.fragment.DeviceListDialogFragment
import com.example.cocobeat.fragment.ProgressDialog
import com.example.cocobeat.model.*
import com.example.cocobeat.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject

import kotlin.coroutines.CoroutineContext


class DevicesActivity : AppCompatActivity(), CoroutineScope, DevicesAdapter.OnItemClickListener {
    private var job: Job = Job()
    private lateinit var binding: ActivityDevicesBinding
    private val repository : DeviceRepository by inject()
    private lateinit var deviceViewModel: DeviceViewModel

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

        turnBluetoothOnIfOff()
        statusCheck()

        var factory = DeviceViewModelFactory(repository)
        deviceViewModel = ViewModelProvider(this, factory)[DeviceViewModel::class.java]

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_device_list_title)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        deviceViewModel.allDevices.observe(this@DevicesActivity, androidx.lifecycle.Observer {
            if(it.isNullOrEmpty()){
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
            }else {
                binding.emptyView.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.apply {
                    adapter = DevicesAdapter(it, this@DevicesActivity, false)
                    layoutManager = LinearLayoutManager(this@DevicesActivity)
                }
            }
        })

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

    private fun openDeviceDialog() {
        var dialog = DeviceListDialogFragment()
        dialog.setOnDialogCloseListener(object : DeviceListDialogFragment.DialogClosedListener {
            override fun onClose(deviceName: String?) {
                var progressDialog = ProgressDialog(deviceName)
                progressDialog.show(supportFragmentManager, "deviceListDialogFragment")
            }
        })
        dialog.show(supportFragmentManager, "deviceListDialogFragment")
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}

