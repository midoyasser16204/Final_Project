package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.databinding.FragmentSignUpBinding
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.viewmodels.UserViewModel

import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class SignUp : Fragment() {
    private lateinit var navController: NavController
    lateinit var binding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.SignUpButton.setOnClickListener {
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signUpUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun signUpUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                userViewModel.Uid=auth.currentUser?.uid
                userViewModel.userData
//                val intent = Intent(requireContext(), CompanyActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
                    findNavController().navigate(R.id.action_signUp_to_select_role)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Sign-Up Failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}