package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSignInBinding
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth

class SignIn : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var cardJobSeeker: MaterialCardView
    private lateinit var cardCompany: MaterialCardView
    lateinit var auth: FirebaseAuth
    var selectRole: String = "joopSeeker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the binding
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        // Set the switch state based on saved language
        binding.language.isChecked = sharedPreferences.getString("LANGUAGE", "en") == "ar"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        cardJobSeeker = binding.cardJobSeeker
        cardCompany = binding.cardCompany

        binding.cardJobSeeker.setOnClickListener {
            selectRole = "joopSeeker"
            selectRole(cardJobSeeker, cardCompany)
        }

        binding.cardCompany.setOnClickListener {
            selectRole = "company"
            selectRole(cardCompany, cardJobSeeker)
        }
        binding.btnSignUp.setOnClickListener {


            navController.navigate(R.id.action_signIn_to_signUp)
        }
        binding.SignInButton.setOnClickListener {
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        // Set up switch listener
        binding.language.setOnClickListener {
            val currentLanguage = sharedPreferences.getString("LANGUAGE", "en") ?: "en"
            val newLanguage = if (currentLanguage == "en") "ar" else "en"
            (activity as? MainActivity)?.applyConfiguration(newLanguage)
            Log.d("LanguageSwitch", "Language changed to: $newLanguage")
        }

    }
    private fun signInUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                    Toast.makeText(requireContext(), "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                if (selectRole == "joopSeeker") {
                    val intent = Intent(requireContext(), DisabilityActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    val intent = Intent(requireContext(), CompanyActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }


            } else {
                Toast.makeText(
                    requireContext(),
                    "Sign-Up Failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun selectRole(selectedCard: MaterialCardView, otherCard: MaterialCardView) {
        selectedCard.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.subheading_color
            )
        )
        otherCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))


    }


}