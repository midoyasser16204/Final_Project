package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.databinding.FragmentSignUpBinding
import android.widget.Toast
import com.example.myapplication.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : Fragment() {
    private lateinit var navController: NavController
    lateinit var binding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.SignUpButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                signUpBinding.Email.toString(),
                signUpBinding.Password.toString()
            )
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {


                        val user = auth.currentUser
                        Toast.makeText(
                            requireContext(),
                            "Authentication success.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
         
        }

    }


}