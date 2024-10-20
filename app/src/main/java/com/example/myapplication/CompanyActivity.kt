package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityCompanyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavController with FragmentContainerView
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView2.id) as NavHostFragment
        navController = navHostFragment.navController

        // Set up BottomNavigationView using binding
        val bottomNavigationView: BottomNavigationView = binding.bottomHomeNav

        // Connect the BottomNavigationView with the NavController
        bottomNavigationView.setupWithNavController(navController)

        // Set up item selection listener

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_job_seekers -> {
                    navController.navigate(R.id.nav_job_seekers)
                    true
                }
                R.id.nav_company_info -> {
                    navController.navigate(R.id.nav_company_info)
                    true
                }
                else -> false
            }
        }


        // Load the default fragment on activity start
        if (savedInstanceState == null) {
            navController.navigate(R.id.nav_company_info)
        }
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("LAST_ACTIVITY", "CompanyActivity").apply()
    }
}