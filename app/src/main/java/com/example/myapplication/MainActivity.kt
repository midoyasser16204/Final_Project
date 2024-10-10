package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
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

        // Initialize NavController here if necessary
        // navController = ...
    }

    fun setLocalization(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        // Update the configuration
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language to SharedPreferences
        sharedPreferences.edit().putString("LANGUAGE", languageCode).apply()

        // Notify fragments to update their UI
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment is LanguageChangeListener) {
                fragment.onLanguageChanged(languageCode)
            }
        }

        // Optional: Refresh other UI elements if needed
//        binding.btnSignUp.text = getString(R.string.Create_Acc) // Refresh specific UI elements if applicable
    }
}
