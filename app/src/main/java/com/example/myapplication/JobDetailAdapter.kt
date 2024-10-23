package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.ItemLayoutTwoBinding

class JobDetailAdapter(
    private var jobList: List<JobData>, // List of disabilities
    private val onDetailClick: (JobData) -> Unit // Callback for item click
) : RecyclerView.Adapter<JobDetailAdapter.JobHolder>() {

    // ViewHolder for each disability item
    inner class JobHolder(val binding: ItemLayoutTwoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(jobData: JobData) {
            binding.user =  jobData// Bind disability data to the layout

            binding.card.setOnClickListener {
                onDetailClick(jobData) // Corrected reference to jobData
            }
        }
    }

    // Create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
        val binding = ItemLayoutTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: JobHolder, position: Int) {
        holder.bindData(jobList[position])
    }

    // Get the item count
    override fun getItemCount() = jobList.size

    // Update the data in the adapter
    fun updateData(newList: List<JobData>) {
        jobList = newList
        notifyDataSetChanged()
    }
}
