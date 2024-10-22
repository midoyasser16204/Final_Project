package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.ItemJobBinding

class JobAdapter(
    private var jobList: List<JobData>,
    private val onDetailClick: ((JobData) -> Unit)? = null,  // Optional click handler
    private val onDeleteClick: ((JobData) -> Unit)? = null   // Optional delete handler
) : RecyclerView.Adapter<JobAdapter.JobHolder>() {

    inner class JobHolder(private val binding: ItemJobBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(jobData: JobData) {
            binding.user = jobData

            // Invoke click handlers only if provided
            binding.card.setOnClickListener {
                onDetailClick?.invoke(jobData)
            }
            binding.delete.setOnClickListener {
                onDeleteClick?.invoke(jobData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobHolder(binding)
    }

    override fun onBindViewHolder(holder: JobHolder, position: Int) {
        holder.bindData(jobList[position])
    }

    override fun getItemCount() = jobList.size

    fun updateData(newList: List<JobData>) {
        jobList = newList
        notifyDataSetChanged()
    }
}
