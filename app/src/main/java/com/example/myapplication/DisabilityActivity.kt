package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityDisabilityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DisabilityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisabilityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisabilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavController with FragmentContainerView
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView3.id) as NavHostFragment
        navController = navHostFragment.navController

        // Set up BottomNavigationView using binding
        val bottomNavigationView: BottomNavigationView = binding.bottomHomeNav

        // Connect the BottomNavigationView with the NavController
        bottomNavigationView.setupWithNavController(navController)

        // Set up item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.jop_sekeer_profile -> {
                    navController.navigate(R.id.jop_sekeer_profile)
                    true
                }
                R.id.find_job -> {
                    navController.navigate(R.id.find_job)
                    true
                }
                else -> false
            }
        }

        // Load the default fragment on activity start
        if (savedInstanceState == null) {
            navController.navigate(R.id.jop_sekeer_profile) // Set the default fragment to `find_job`
        }

        // Save the current activity in SharedPreferences
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("LAST_ACTIVITY", "DisabilityActivity").apply()
    }
}
