package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class DisabilityNotificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_disability_notification, container, false)
    }

    fun updateUIWithNotification(title: String, body: String) {
        Toast.makeText(requireContext(), "Fragment: $title - $body", Toast.LENGTH_SHORT).show()
        // Update your fragment’s UI components if necessary
    }
}
