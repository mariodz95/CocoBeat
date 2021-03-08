package com.example.cocobeat.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocobeat.adapter.ExerciseAdapter
import com.example.cocobeat.database.entity.Exercise
import com.example.cocobeat.databinding.FragmentRecentBinding
import com.example.cocobeat.model.ExerciseViewModel
import com.example.cocobeat.model.ExerciseViewModelFactory
import com.example.cocobeat.repository.ExerciseRepository
import com.example.cocobeat.view.DateAndTime
import org.koin.android.ext.android.inject
import java.util.*

class RecentFragment : Fragment(){

    private lateinit var exerciseViewModel: ExerciseViewModel
    private val repository : ExerciseRepository by inject()

    private var _binding: FragmentRecentBinding? = null
    private val binding get() = _binding!!

    lateinit var mTime: String
    lateinit var mDate: Date

    var hashMap:HashMap<Int, Exercise> = HashMap<Int, Exercise>()

    lateinit var onExerciseAdded: RecentFragment.OnExerciseAddedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onExerciseAdded = context as OnExerciseAddedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var factory = ExerciseViewModelFactory(repository)
        exerciseViewModel = ViewModelProvider(requireActivity(), factory)[ExerciseViewModel::class.java]
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        exerciseViewModel.getRecentExercise()

        binding.dateAndTimeView.setDateAndTimeListener(object :
            DateAndTime.OnDateAndTImeChangeListener {
            override fun getDateAndTime(date: Date, time: String) {
                mDate = date
                mTime = time
            }
        })

        exerciseViewModel.recentExercise?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var exerciseAdapter = ExerciseAdapter(it)
            exerciseAdapter.setOnCheckListener((object : ExerciseAdapter.OnItemCheckListener {
                override fun onItemCheck(exercise: Exercise, position: Int) {
                    var newExercise = Exercise(
                        UUID.randomUUID(),
                        exercise.exerciseName,
                        exercise.hourDuration,
                        exercise.minuteDuration,
                        mDate,
                        mTime
                    )
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