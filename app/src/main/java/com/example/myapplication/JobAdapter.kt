package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.ItemJobBinding

class JobAdapter(
    private var jobList: List<JobData>, // List of jobs
    private val onDetailClick: (JobData) -> Unit, // Callback for item click
    private val onDeleteClick: (JobData) -> Unit // Callback for delete click
) : RecyclerView.Adapter<JobAdapter.JobHolder>() {

    // ViewHolder for each job item
    inner class JobHolder(val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(jobData: JobData) {
            binding.user = jobData // Bind job data to the layout

            // Handle detail click event
            binding.card.setOnClickListener {
                onDetailClick(jobData)
            }

            // Handle delete click event
            binding.delete.setOnClickListener {
                onDeleteClick(jobData)
            }
        }
    }

    // Create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
        // Correctly inflate the layout using ItemJobBinding, not ItemLayoutBinding
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: JobHolder, position: Int) {
        holder.bindData(jobList[position])
    }

    // Get the item count
    override fun getItemCount() = jobList.size

    fun updateData(newList: List<JobData>) {
        jobList = newList
        notifyDataSetChanged() // Notify that the data has changed
    }

}
