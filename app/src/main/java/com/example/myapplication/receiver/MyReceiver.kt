package com.example.myapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    // This method is called when the broadcast is received
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve the message from the Intent extras
        val message = intent.getStringExtra("message") ?: "No message"
        Log.d("NotificationReceiver", "Received message: $message")

        // Show a toast as feedback (optional)
        Toast.makeText(context, "Action: $message", Toast.LENGTH_SHORT).show()
    }
}
