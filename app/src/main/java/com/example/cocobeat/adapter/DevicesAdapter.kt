package com.example.cocobeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocobeat.database.entity.Device
import com.example.cocobeat.databinding.DeviceRowItemBinding
import java.text.SimpleDateFormat


class DevicesAdapter(
    private val deviceList: List<Device>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {

    private var _binding: DeviceRowItemBinding? = null
    private val binding get() = _binding!!

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = DeviceRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(deviceList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return deviceList.size
    }

    //the class is holding the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position !=  RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        fun bindItems(device: Device) {
            binding.deviceImage.setImageResource((device.imageResource))
            binding.deviceName.text = device.name
            if(device.lastSyncDate != null) {
                val format = SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a")
                binding.deviceSyncDate.text = format.format(device.lastSyncDate.time)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}