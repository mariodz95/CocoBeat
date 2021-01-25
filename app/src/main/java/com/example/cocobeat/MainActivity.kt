package com.example.cocobeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Called when the user taps the Device button */
    fun openDevicesActivity(view: View) {
        val intent = Intent(this, DevicesActivity::class.java )
        startActivity(intent)
    }
}