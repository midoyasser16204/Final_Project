package com.example.myapplication

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import com.example.myapplication.receiver.MyReceiver

object NotificationModule {

    // Method to build and provide a NotificationCompat.Builder instance
    fun provideNotificationBuilder(context: Context): NotificationCompat.Builder {
        // Intent to handle the broadcast action via MyReceiver
        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("message", "Clicked")  // Send extra data with the broadcast
        }

        // Set the appropriate flag based on API level
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag)

        // Build the notification
        return NotificationCompat.Builder(context, "Main Channel ID")
            .setContentTitle("Welcome")            // Set the notification title
            .setContentText("Hello, welcome to the app!")  // Set the notification body
            .setSmallIcon(R.drawable.baseline_notifications_24)  // Ensure the icon exists
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)   // Set priority
            .setVisibility(VISIBILITY_PRIVATE)    // Set visibility to private
            .setPublicVersion(
                NotificationCompat.Builder(context, "Main Channel ID")
                    .setContentTitle("Hidden")  // Title for public version
                    .setContentText("Unlock to see the message")  // Public version text
                    .build()
            )
            .addAction(
                R.drawable.baseline_notifications_24,  // Use a valid drawable resource
                "Action",                             // Action text
                pendingIntent                         // PendingIntent for action
            )
    }
}
