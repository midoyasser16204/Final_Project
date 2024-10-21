package com.example.myapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import com.example.myapplication.receiver.MyReceiver

object NotificationModule {

    fun provideNotificationBuilder(context: Context): NotificationCompat.Builder {
        // Intent to handle the broadcast action
        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("message", "Clicked")
        }

        // Set the flag based on API level
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag)

        // Build the notification
        return NotificationCompat.Builder(context, "Main Channel ID")
            .setContentTitle("Welcome")
            .setContentText("Hello, welcome to the app!")
            .setSmallIcon(R.drawable.baseline_notifications_24) // Ensure the icon exists
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_PRIVATE)
            .setPublicVersion(
                NotificationCompat.Builder(context, "Main Channel ID")
                    .setContentTitle("Hidden")
                    .setContentText("Unlock to see the message")
                    .build()
            )
            .addAction(
                R.drawable.baseline_notifications_24, // Use a valid drawable resource
                "Action",
                pendingIntent
            )
    }
}
