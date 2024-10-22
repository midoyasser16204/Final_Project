package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.CompanyData
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.data.model.Model
import com.example.myapplication.databinding.FragmentCompanyInformationBinding
import com.example.myapplication.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class company_information : Fragment() {
    lateinit var binding: FragmentCompanyInformationBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fireAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firestore = FirebaseFirestore.getInstance()
        fireAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        binding = FragmentCompanyInformationBinding.inflate(inflater, container, false)

        binding.logout.setOnClickListener {
            logout()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Store the companyId in the ViewModel
        binding.save.setOnClickListener {
            val companyName = binding.CompanyName.text.toString()
            val companyPhone = binding.phone.text.toString()
            val companyEmail = binding.Email.text.toString()
            val companyAddress = binding.Location.text.toString()
            val companyWebsite = binding.URL.text.toString()
            val companyDescription = binding.Disc.text.toString()

            val companyInformation = CompanyData(
                companyName = companyName,
                phone = companyPhone,
                email = companyEmail,
                location = companyAddress,
                websiteUrl = companyWebsite,
                description = companyDescription
            )
            saveDisabilityData(companyInformation)
        }

        // Setting up spinner
        val disabilityArray = resources.getStringArray(R.array.Company_Array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, disabilityArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.IndustryTypespinner.adapter = adapter
    }

    fun saveDisabilityData(companyData: CompanyData) {
        val docRef = firestore.collection("companyData").document(fireAuth.currentUser?.uid.toString())

        docRef.set(companyData)
            .addOnSuccessListener {
                Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logout() {
        fireAuth.signOut()

        // Clear SharedPreferences if needed
        val sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Navigate to MainActivity
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
