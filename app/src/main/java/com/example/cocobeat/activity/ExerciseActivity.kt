package com.example.cocobeat.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.cocobeat.R
import com.example.cocobeat.adapter.SectionsPagerAdapter
import com.example.cocobeat.database.entity.Exercise
import com.example.cocobeat.databinding.ActivityExerciseBinding
import com.example.cocobeat.fragment.FrequentFragment
import com.example.cocobeat.fragment.RecentFragment
import com.example.cocobeat.model.ExerciseViewModel
import com.example.cocobeat.model.ExerciseViewModelFactory
import com.example.cocobeat.repository.ExerciseRepository
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(), RecentFragment.OnExerciseAddedListener, FrequentFragment.OnExerciseAddedListener {
    private lateinit var binding: ActivityExerciseBinding

    private val repository : ExerciseRepository by inject()
    private lateinit var exerciseViewModel: ExerciseViewModel

    private var currentSelectedItems: MutableList<Exercise> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var factory = ExerciseViewModelFactory(repository)
        exerciseViewModel = ViewModelProvider(this, factory)[ExerciseViewModel::class.java]

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.exercise_title)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val done: Toolbar = findViewById(R.id.done)
        done?.setOnClickListener {
            exerciseViewModel.insertExercises(currentSelectedItems)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    override fun getExercises(hashMap: HashMap<Int, Exercise>) {
        currentSelectedItems = arrayListOf<Exercise>()
        currentSelectedItems.addAll(hashMap.values)
    }
}