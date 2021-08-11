package com.delion.blelogger.view.fragment

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.delion.blelogger.DeviceEntry
import com.delion.blelogger.R
import com.delion.blelogger.databinding.FragmentDeviceEntryBinding

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class RecordingRecyclerViewAdapter(
    private val values: List<DeviceEntry>,
) : RecyclerView.Adapter<RecordingRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentDeviceEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val time = LocalDateTime.ofEpochSecond(item.timestamp / 1000000000, (item.timestamp % 1000000000).toInt(), ZoneOffset.ofHours(9))
        holder.timestampView.text = time.format(DateTimeFormatter.ofPattern("hh:mm:ss"))
        holder.idView.text = item.id.toString()
        holder.rssiView.text = item.rssi.toString()
        holder.powerView.text = item.power.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentDeviceEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        val timestampView: TextView = binding.timestamp
        val idView: TextView = binding.id
        val rssiView: TextView = binding.rssi
        val powerView: TextView = binding.power

        override fun toString(): String {
            return super.toString() + " '" + idView.text + "'"
        }
    }

}