package com.example.myapplication

import LanguageViewModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
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
    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("LANGUAGE", "en") ?: "en"
        setLocalization(savedLanguage)
        setContentView(binding.root)
    }

    fun setLocalization(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
        sharedPreferences.edit().putString("LANGUAGE", languageCode).apply()
        languageViewModel.setLanguage(languageCode)
    }
}
