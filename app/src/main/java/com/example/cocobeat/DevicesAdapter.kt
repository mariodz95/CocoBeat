package com.example.cocobeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DevicesAdapter(
    private val deviceList: ArrayList<DeviceDataModel>,
    private val listener:  OnItemClickListener
) : RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_row_item, parent, false)
        return ViewHolder(itemView)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(deviceList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return deviceList.size
    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.device_image)
        val textView1: TextView = itemView.findViewById(R.id.device_name)
        val textView2: TextView = itemView.findViewById(R.id.device_sync_date)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val postition: Int = adapterPosition
            if(postition !=  RecyclerView.NO_POSITION) {
                listener.onItemClick(postition)
            }
        }

        fun bindItems(device: DeviceDataModel) {
            imageView.setImageResource((device.imageResource))
            textView1.text = device.deviceName
            textView2.text = device.deviceSyncData
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}