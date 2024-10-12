package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("OnCreate", "Activity Recreated")

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Retrieve and set the saved language
        val savedLanguage = sharedPreferences.getString("LANGUAGE", "en") ?: "en"
        setLocalization(savedLanguage)

        setContentView(binding.root)

        // Initialize NavController here if necessary
        // navController = ...
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

