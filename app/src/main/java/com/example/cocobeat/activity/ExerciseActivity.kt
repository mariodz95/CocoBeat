package com.example.cocobeat.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.cocobeat.R
import com.example.cocobeat.adapter.SectionsPagerAdapter
import com.example.cocobeat.databinding.ActivityDevicesBinding
import com.example.cocobeat.databinding.ActivityExerciseBinding
import com.google.android.material.tabs.TabLayout
import java.lang.Exception


class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.exercise_title)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        binding.btnAddExercise.setOnClickListener(View.OnClickListener {
            openCreateExerciseActivity()
        })

    }


    private fun openCreateExerciseActivity() {
        val intent = Intent(this, CreateExerciseActivity::class.java)
        startActivity(intent)
    }
}