package com.example.cocobeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocobeat.R
import com.example.cocobeat.databinding.HistoryRowLayoutBinding
import com.example.cocobeat.model.HistoryItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat


class HistoryAdapter(
    private val historyItems: List<HistoryItem>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var _binding: HistoryRowLayoutBinding? = null

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = HistoryRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val view = _binding!!.root
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(historyItems[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return historyItems.size
    }

    //the class is holding the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(historyItem: HistoryItem) {
            val format = SimpleDateFormat("MMMM d, yyyy")
            if (historyItem.type == "Reading") {
                _binding?.imageView?.setBackgroundResource(R.drawable.ic_baseline_bloodtype_24)
                _binding?.txtName?.text = "${BigDecimal(historyItem.value!!).setScale(
                    2,
                    RoundingMode.HALF_EVEN
                )} ${historyItem.unit}"
                _binding?.txtDate?.text = "${format.format(historyItem.date)}"
            } else if (historyItem.type == "Exercise") {
                _binding?.imageView?.setBackgroundResource(R.drawable.ic_baseline_directions_run_24)
                _binding?.txtName?.text =
                    "${historyItem.name} ${historyItem.hourDuration} hour ${historyItem.minuteDuration}"
                _binding?.txtDate?.text = "${format.format(historyItem.date)}"
            }
        }
    }
}