package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
                R.id.nav_profile -> { // Update this ID
                    navController.navigate(R.id.nav_profile)
                    true
                }
                R.id.nav_not -> {
                    navController.navigate(R.id.nav_not)
                    true
                }
                else -> false
            }
        }

        // Load the default fragment on activity start
        if (savedInstanceState == null) {
            navController.navigate(R.id.nav_profile) // Use the correct ID here
        }
    }

}