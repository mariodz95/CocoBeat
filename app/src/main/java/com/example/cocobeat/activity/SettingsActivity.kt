package com.example.cocobeat.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.cocobeat.R
import com.example.cocobeat.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.settings_title)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val prefs = getSharedPreferences("SETTINGS", MODE_PRIVATE)
        val monthSteps = prefs.getInt("monthSteps", 0)
        binding.stepsInput.setText(monthSteps.toString())

        val doneBtn: Toolbar = findViewById(R.id.done)
        doneBtn.setOnClickListener {
            val prefs = getSharedPreferences("SETTINGS", MODE_PRIVATE).edit()
            prefs.putInt("monthSteps", binding.stepsInput.text.toString().toInt())
            prefs.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}