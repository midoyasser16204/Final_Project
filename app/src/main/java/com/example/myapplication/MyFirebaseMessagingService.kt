package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")

        // Use Handler to display Toast on UI thread
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, "New token generated: $token", Toast.LENGTH_SHORT).show()
        }

        sendTokenToFirestore(token)
    }

    // Called when a message is received from FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message from: ${remoteMessage.from}")

        // Check if message contains a notification payload
        if (remoteMessage.notification != null) {
            // Extract notification title and body, or provide defaults
            val title = remoteMessage.notification?.title ?: "No Title"
            val body = remoteMessage.notification?.body ?: "No Body"

            // Display the notification
            sendNotification(title, body)
        } else {
            Log.d("FCM", "No notification payload")
        }
    }

    // Method to build and display a notification
    private fun sendNotification(title: String, body: String) {
        // Intent to open MainActivity when the notification is tapped
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("title", title)
            putExtra("body", body)
        }

        // PendingIntent to open MainActivity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Notification Manager instance
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id" // Notification channel ID

        // Create a notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // Notification title
            .setContentText(body)   // Notification body
            .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your app's notification icon
            .setContentIntent(pendingIntent) // PendingIntent
            .setAutoCancel(true)  // Dismiss notification on tap
            .build()

        // Show the notification with a unique ID (e.g., timestamp-based)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
    private fun sendTokenToFirestore(token: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userId?.let {
            val tokenData = mapOf("fcmToken" to token)
            firestore.collection("users").document(it)
                .set(tokenData, SetOptions.merge())
                .addOnSuccessListener { Log.d("FCM", "Token saved successfully.") }
                .addOnFailureListener { e -> Log.e("FCM", "Error saving token", e) }
        } ?: Log.w("FCM", "User not logged in. Token not saved.")
    }

}
