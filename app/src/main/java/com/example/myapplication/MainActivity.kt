//package com.example.myapplication
//
//import LanguageViewModel
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.NavController
//import com.example.myapplication.databinding.ActivityMainBinding
//import java.util.Locale
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var navController: NavController
//    private val binding by lazy(LazyThreadSafetyMode.NONE) {
//        ActivityMainBinding.inflate(layoutInflater)
//    }
//    private lateinit var sharedPreferences: SharedPreferences
//    private val languageViewModel: LanguageViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        val savedLanguage = sharedPreferences.getString("LANGUAGE", "en") ?: "en"
//        setLocalization(savedLanguage)
//        setContentView(binding.root)
//    }
//
//    fun setLocalization(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val config = resources.configuration
//        config.setLocale(locale)
//        createConfigurationContext(config)
//        resources.updateConfiguration(config, resources.displayMetrics)
//        sharedPreferences.edit().putString("LANGUAGE", languageCode).apply()
//        languageViewModel.setLanguage(languageCode)
//    }
//}
package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.databinding.ActivityMainBinding

import java.util.Locale

import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    lateinit var  auth: FirebaseAuth
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load saved language preference
        loadLocalization()

        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
    }

    // Function to change language programmatically
    fun setLocalization(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val res = context.resources
        val config = res.configuration
        config.setLocale(locale)

        res.updateConfiguration(config, res.displayMetrics)

        // Save new language preference
        saveLocalization(languageCode)

        // Broadcast the language change event
        val intent = Intent("LANGUAGE_CHANGED")
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun loadLocalization() {
        // Load the saved language from shared preferences (or set default)
        val sharedPrefs = getSharedPreferences("language_settings", Context.MODE_PRIVATE)
        val languageCode = sharedPrefs.getString("selected_language", "en") // default to English
        setLocalization(this, languageCode ?: "en")
    }

    private fun saveLocalization(languageCode: String) {
        val sharedPrefs = getSharedPreferences("language_settings", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString("selected_language", languageCode)
            apply()
        }
    }
}

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    if (currentUser != null) {

    }
    }
}

