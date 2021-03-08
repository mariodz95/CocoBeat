package com.example.cocobeat.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.R
import com.example.cocobeat.activity.MainActivity
import com.example.cocobeat.adapter.ExerciseAdapter
import com.example.cocobeat.database.entity.Exercise
import com.example.cocobeat.databinding.FragmentFrequentBinding
import com.example.cocobeat.model.ExerciseViewModel
import com.example.cocobeat.model.ExerciseViewModelFactory
import com.example.cocobeat.repository.ExerciseRepository
import com.example.cocobeat.view.DateAndTime
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FrequentFragment : Fragment() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private val repository : ExerciseRepository by inject()

    private var _binding: FragmentFrequentBinding? = null
    private val binding get() = _binding!!

    lateinit var mTime: String
    lateinit var mDate: Date

    val hashMap: HashMap<Int, Exercise> = HashMap<Int, Exercise>()

    lateinit var onExerciseAdded: RecentFragment.OnExerciseAddedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onExerciseAdded = context as RecentFragment.OnExerciseAddedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var factory = ExerciseViewModelFactory(repository)
        exerciseViewModel = ViewModelProvider(requireActivity(), factory)[ExerciseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFrequentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        exerciseViewModel.getFrequentExercise()

        binding.dateAndTimeView.setDateAndTimeListener(object : DateAndTime.OnDateAndTImeChangeListener {
            override fun getDateAndTime(date: Date, time: String) {
                mDate = date
                mTime = time
            }
        })
        exerciseViewModel.frequentExercise?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var exerciseAdapter = ExerciseAdapter(it)
            exerciseAdapter.setOnCheckListener((object : ExerciseAdapter.OnItemCheckListener {
                override fun onItemCheck(exercise: Exercise, position: Int) {
                    var newExercise = Exercise(UUID.randomUUID(), exercise.exerciseName, exercise.hourDuration, exercise.minuteDuration, mDate, mTime)
                    hashMap[position] = newExercise
                    onExerciseAdded.getExercises(hashMap)
                }
                override fun onItemUncheck(exercise: Exercise, position: Int) {
                    hashMap.remove(position)
                    onExerciseAdded.getExercises(hashMap)
                }
            }))
            binding.recyclerView.adapter = exerciseAdapter
        })
        return binding.root
    }

    interface OnExerciseAddedListener{
        fun getExercises(hashMap:HashMap<Int, Exercise> )
    }
}