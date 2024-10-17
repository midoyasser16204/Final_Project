package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.Locale
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Retrieve and set the saved language
        val savedLanguage = sharedPreferences.getString("LANGUAGE", "en") ?: "en"
        setLocalization(savedLanguage)

        setContentView(binding.root)

        // Initialize Firebase and get the device token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", "Device Token: $token")
            } else {
                Log.e("FCM Token", "Fetching FCM token failed", task.exception)
            }
        }

        // Handle notifications when the app is open
        handleIncomingNotification(intent.extras)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleIncomingNotification(intent.extras)
    }

    private fun handleIncomingNotification(data: Bundle?) {
        data?.let {
            val title = it.getString("title", "No Title")
            val body = it.getString("body", "No Body")

            Toast.makeText(this, "Notification: $title - $body", Toast.LENGTH_LONG).show()
            // Pass the data to the active fragment (if needed)
            sendDataToFragment(title, body)
        }
    }

    private fun sendDataToFragment(title: String, body: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (fragment is DisabilityNotificationFragment) {
            fragment.updateUIWithNotification(title, body)
        }
    }

    fun setLocalization(languageCode: String) {
        val config = resources?.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config?.setLocale(locale)
        resources?.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language to SharedPreferences
        sharedPreferences.edit().putString("LANGUAGE", languageCode).apply()
    }

    fun applyConfiguration(languageCode: String) {
        setLocalization(languageCode)
        recreate()
    }
}
