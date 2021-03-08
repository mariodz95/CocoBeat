package com.example.cocobeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocobeat.database.entity.Exercise
import com.example.cocobeat.databinding.ExerciseRowLayoutBinding


class ExerciseAdapter(
    private val exerciseList: List<Exercise>
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    private var _binding: ExerciseRowLayoutBinding? = null

    lateinit var  onItemCheckListener: OnItemCheckListener

    fun setOnCheckListener(listener: ExerciseAdapter.OnItemCheckListener) {
        onItemCheckListener = listener
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ExerciseRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val view = _binding!!.root
        return ViewHolder(view)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(exerciseList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return exerciseList.size
    }

    //the class is holding the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        init{
            _binding?.check?.setOnCheckedChangeListener { buttonView, isChecked ->
                val position: Int = adapterPosition
                val exercise = exerciseList[position]
                if(isChecked) {
                    onItemCheckListener.onItemCheck(exercise, position);

                }else{
                    onItemCheckListener.onItemUncheck(exercise, position);
                }
            }
        }

        override fun onClick(v: View) {
        }
        
        fun bindItems(exercise: Exercise) {
            _binding?.name?.text = exercise.exerciseName
            _binding?.duration?.text = "${exercise.hourDuration} hour ${exercise.minuteDuration} minutes"
        }
    }

    interface OnItemCheckListener{
        fun onItemCheck(exercise: Exercise, position: Int)
        fun onItemUncheck(exercise: Exercise, position: Int)
    }
}