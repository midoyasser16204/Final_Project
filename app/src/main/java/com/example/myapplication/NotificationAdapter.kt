package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.NotificationData
import com.example.myapplication.databinding.ItemNotBinding

class NotificationAdapter(
    private var notificationList: MutableList<NotificationData>, // List of notifications
    private val onDetailClick: (NotificationData) -> Unit // Callback for item click
) : RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {

    // ViewHolder for each notification item
    inner class NotificationHolder(val binding: ItemNotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(notificationData: NotificationData) {
            binding.user = notificationData // Bind notification data to the layout

            // Handle detail click event
            binding.card.setOnClickListener {
                onDetailClick(notificationData)
            }
        }
    }

    // Create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val binding = ItemNotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bindData(notificationList[position])
    }

    // Get the item count
    override fun getItemCount() = notificationList.size

    // Update the data in the adapter
    fun updateData(newList: List<NotificationData>) {
        notificationList = newList.toMutableList()
        notifyDataSetChanged()
    }
}
