package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.FragmentJobSeekerCardDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class JobSeekerCardDetail : Fragment() {

    private lateinit var binding: FragmentJobSeekerCardDetailBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobSeekerCardDetailBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        // Get the user ID passed from the previous fragment
        val userId = arguments?.getString("userId")

        if (userId != null) {
            fetchDisabilityData(userId)
        } else {
            Toast.makeText(context, "No user ID found", Toast.LENGTH_SHORT).show()
        }

        // Set click listener for "Apply" button to trigger notifications
        binding.btnApply.setOnClickListener {
            val userId = arguments?.getString("userId") ?: return@setOnClickListener
            fetchUserTokenAndSendNotification(userId) // Handles Firebase Token (optional)
        }

        createNotificationChannel() // Create notification channel for Android 8.0+

        return binding.root
    }

    // Fetch disability data from Firestore using the provided user ID
    private fun fetchDisabilityData(userId: String) {
        firestore.collection("disabilityData").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val disabilityData = documentSnapshot.toObject(DisabilityData::class.java)
                    if (disabilityData != null) {
                        Log.d("DisabilityData", "Data fetched: $disabilityData")

                        // Bind the data to the UI elements
                        binding.name.text = disabilityData.name
                        binding.age.text = disabilityData.age.toString()
                        binding.phone.text = disabilityData.phone
                        binding.email.text = disabilityData.email
                        binding.skill.text = disabilityData.skill
                        binding.address.text = disabilityData.address
                        binding.disability.text = disabilityData.disability

                        // Load profile image using Glide
                        disabilityData.profileImageUrl?.let { imageUrl ->
                            Log.d("ImageLoad", "Loading image from URL: $imageUrl")
                            Glide.with(this)
                                .load(imageUrl)
                                .error(R.drawable.ic_image_picker) // Fallback image
                                .into(binding.profileImage)
                        } ?: binding.profileImage.setImageResource(R.drawable.ic_image_picker)

                        // Set up PDF preview click listener
                        disabilityData.pdfUrl?.let { pdfUrl ->
                            binding.pdfPreview.setOnClickListener { openPdf(pdfUrl) }
                        }
                    } else {
                        Toast.makeText(context, "Failed to map data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch data: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("DisabilityData", "Error fetching data", exception)
            }
    }

    // Open PDF in an external viewer
    private fun openPdf(pdfUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(pdfUrl), "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }

// Send notification to the user
    private fun sendNotification() {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationModule.provideNotificationBuilder(requireContext())
            .setContentTitle("Job Application")
            .setContentText("You've been contacted")
            .setAutoCancel(true) // Dismiss when clicked
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setChannelId("default_channel_id") // Ensure this matches the one created in createNotificationChannel

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }


    // Create a notification channel for Android 8.0+ (Oreo)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "your_channel_id"
            val channelName = "Job Notifications"
            val channelDescription = "Notifications related to job applications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // (Optional) Fetch Firebase token and send notifications
    private fun fetchUserTokenAndSendNotification(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val token = document.getString("token")
                if (token != null) {
                    Log.d("Notification", "Sending notification to user with token: $token")
                    sendNotification() // Trigger the notification
                } else {
                    Toast.makeText(context, "User token not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch user token: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
