package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.NotificationData

class DisabilityNotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter
    private val notificationsList = mutableListOf<NotificationData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_disability_notification, container, false)
        recyclerView = view.findViewById(R.id.notificationsRecyclerView)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationAdapter(notificationsList) { notification ->
            // Handle detail click, you can open a detailed view or perform any action here
            Toast.makeText(requireContext(), "Clicked: ${notification.title}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        return view
    }

    fun updateUIWithNotification(title: String, body: String) {
        // Add the notification to the list
        val notification = NotificationData(title, body) // Ensure NotificationData has title and body
        notificationsList.add(notification)
        adapter.notifyItemInserted(notificationsList.size - 1)  // Notify adapter of new item
    }
}
