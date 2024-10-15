package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.ItemLayoutBinding

class DisabilityAdapter(
    private var disabilityList: List<DisabilityData>, // List of disabilities
    private val onDetailClick: (DisabilityData) -> Unit // Callback for item click
) : RecyclerView.Adapter<DisabilityAdapter.DisabilityHolder>() {

    // ViewHolder for each disability item
    inner class DisabilityHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(disabilityData: DisabilityData) {
            binding.user = disabilityData // Bind disability data to the layout

            // Handle detail click event
            binding.card.setOnClickListener {
                onDetailClick(disabilityData)
            }
        }
    }

    // Create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisabilityHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisabilityHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: DisabilityHolder, position: Int) {
        holder.bindData(disabilityList[position])
    }

    // Get the item count
    override fun getItemCount() = disabilityList.size

    // Update the data in the adapter
    fun updateData(newList: List<DisabilityData>) {
        disabilityList = newList
        notifyDataSetChanged()
    }
}
