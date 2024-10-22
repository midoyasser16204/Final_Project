package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Locale

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

        // Set the content view once
        setContentView(binding.root)

        // Initialize Firebase and get the device token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", "Device Token: $token")
                // Save the token if necessary
                saveTokenToPreferences(token)
            } else {
                Log.e("FCM Token", "Fetching FCM token failed", task.exception)
            }
        }

        // Handle notifications when the app is open
        val lastActivity = sharedPreferences.getString("LAST_ACTIVITY", "MainActivity")

        // Navigate to the last activity
        when (lastActivity) {
            "CompanyActivity" -> {
                startActivity(Intent(this, CompanyActivity::class.java))
                finish()  // Close MainActivity
            }
            "DisabilityActivity" -> {
                startActivity(Intent(this, DisabilityActivity::class.java))
                finish()  // Close MainActivity
            }
        }
    }

    private fun saveTokenToPreferences(token: String) {
        // Optionally save the token in SharedPreferences
        sharedPreferences.edit().putString("FCM_TOKEN", token).apply()
    }

    private fun setLocalization(languageCode: String) {
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language to SharedPreferences
        sharedPreferences.edit().putString("LANGUAGE", languageCode).apply()
    }

    fun applyConfiguration(languageCode: String) {
        setLocalization(languageCode)
        recreate()
    }
}
