package com.example.cocobeat

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.cocobeat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {
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
    }

    private fun openDevicesActivity() {
        val intent = Intent(this, DevicesActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
/*        when(v?.id) {
            R.id.img_arrow_back -> getPrevMonth()
            R.id.img_arrow_next -> getNextMonth()
            R.id.btn_devices -> openDevicesActivity()
        }*/
    }
}