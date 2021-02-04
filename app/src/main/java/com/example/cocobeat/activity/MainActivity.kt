package com.example.cocobeat.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.cocobeat.R
import com.example.cocobeat.databinding.ActivityMainBinding
import com.example.cocobeat.model.MainActivityViewModel


class MainActivity : AppCompatActivity(){
    private val model: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.toolbar_title_layout)
        }

        binding.btnDevices.setOnClickListener{
            openDevicesActivity()
        }
    }

    private fun openDevicesActivity() {
        val intent = Intent(this, DevicesActivity::class.java)
        startActivity(intent)
    }
}