//package com.example.myapplication
//
//import LanguageViewModel
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.NavController
//import androidx.navigation.Navigation
//import com.example.myapplication.databinding.FragmentSignInBinding
//
//class SignInFragment : Fragment(), LanguageChangeListener {
//    private lateinit var navController: NavController
//    private lateinit var binding: FragmentSignInBinding
//    private lateinit var sharedPreferences: SharedPreferences
//    private val languageViewModel: LanguageViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentSignInBinding.inflate(inflater, container, false)
//        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        binding.languageSwitch.isChecked = sharedPreferences.getString("LANGUAGE", "en") == "ar"
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
//        binding.btnSignUp.setOnClickListener {
//            navController.navigate(R.id.action_signIn_to_signUp)
//        }
//
//        binding.languageSwitch.setOnCheckedChangeListener { _, isChecked ->
//            val newLanguage = if (isChecked) "ar" else "en"
//            (activity as? MainActivity)?.setLocalization(newLanguage)
//        }
//
//        languageViewModel.languageLiveData.observe(viewLifecycleOwner) { languageCode ->
//            updateUI(languageCode)
//        }
//    }
//
//    override fun onLanguageChanged(languageCode: String) {
//        updateUI(languageCode)
//    }
//
//    private fun updateUI(languageCode: String) {
//        binding.btnSignUp.text = getString(R.string.Create_Acc)
//    }
//}
package com.example.myapplication

import LanguageViewModel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.databinding.FragmentSignInBinding

class SignInFragment : Fragment(), LanguageChangeListener {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val languageViewModel: LanguageViewModel by activityViewModels()
    private lateinit var languageChangeReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        binding.languageSwitch.isChecked = sharedPreferences.getString("LANGUAGE", "en") == "ar"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        // Navigate to SignUpFragment
        binding.btnSignUp.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_signUp)
        }

        // Language switch logic
        binding.languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            val newLanguage = if (isChecked) "ar" else "en"
            (activity as? MainActivity)?.setLocalization(requireContext(),newLanguage)
        }

        // Observing the ViewModel for language changes
        languageViewModel.languageLiveData.observe(viewLifecycleOwner) { languageCode ->
            updateUI(languageCode)
        }

        // Register BroadcastReceiver to listen for language changes
        languageChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateUI(sharedPreferences.getString("LANGUAGE", "en") ?: "en")
            }
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            languageChangeReceiver,
            IntentFilter("LANGUAGE_CHANGED")
        )

        // Initially set views based on the current language
        updateUI(sharedPreferences.getString("LANGUAGE", "en") ?: "en")
    }

    override fun onLanguageChanged(languageCode: String) {
        updateUI(languageCode)
    }

    private fun updateUI(languageCode: String) {
        // Update UI components based on the language code
        binding.btnSignUp.text = getString(R.string.Create_Acc)
        binding.Email.setText(getString(R.string.Email))
        binding.Password.setText(getString(R.string.Password))
        binding.SignInButton.text = getString(R.string.Sign_In)
        binding.SignIn.text = getString(R.string.Sign_In)
        // Update other views as needed based on language
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Unregister BroadcastReceiver to avoid memory leaks
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(languageChangeReceiver)
    }
}
