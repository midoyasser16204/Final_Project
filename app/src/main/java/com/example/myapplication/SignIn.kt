package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.databinding.FragmentSignInBinding

class SignIn : Fragment(), LanguageChangeListener {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the binding
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        // Set the switch state based on saved language
        binding.languageSwitch.isChecked = sharedPreferences.getString("LANGUAGE", "en") == "ar"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnSignUp.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_signUp)
        }

        // Set up switch listener
        binding.languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            val newLanguage = if (isChecked) "ar" else "en"
            (activity as? MainActivity)?.setLocalization(newLanguage)
        }
    }

    override fun onLanguageChanged(languageCode: String) {
        // Update UI elements based on the new language
        updateUI(languageCode)
    }

    private fun updateUI(languageCode: String) {
        binding.btnSignUp.text = getString(R.string.Create_Acc) // Update button text
        // Update other UI elements here as necessary
    }

}
