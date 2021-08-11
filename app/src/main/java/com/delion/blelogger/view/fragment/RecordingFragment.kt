package com.delion.blelogger.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.delion.blelogger.R
import com.delion.blelogger.databinding.FragmentRecordingBinding
import com.delion.blelogger.viewmodel.MainViewModel

class RecordingFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentRecordingBinding.inflate(inflater, container, false)
        val listAdapter = RecordingRecyclerViewAdapter(viewModel.logger!!.records)
        viewModel.scanner!!.onScanResult = { device ->
            val correctTimestamp = (System.currentTimeMillis() - SystemClock.elapsedRealtime()) * 1000000 + device.timestamp
            viewModel.logger!!.log(device.copy(timestamp = correctTimestamp))
        }
        viewModel.logger!!.onLogged = {
            listAdapter.notifyItemInserted(listAdapter.itemCount - 1)
            binding.list.post {
                binding.list.smoothScrollToPosition(listAdapter.itemCount - 1)
            }
        }
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.fab.setOnClickListener { view ->
            if(viewModel.scanner!!.enabled) {
                binding.fab.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24)
                viewModel.scanner!!.disable()
                viewModel.logger!!.commit(requireContext().getExternalFilesDir(null)!!)
            }
            else {
                binding.fab.setImageResource(R.drawable.ic_baseline_bluetooth_searching_24)
                viewModel.logger!!.reset()
                listAdapter.notifyDataSetChanged()
                viewModel.scanner!!.enable()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.logger!!.onLogged = null
        viewModel.scanner!!.onScanResult = null
    }
}