package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.data.model.CompanyData
import com.example.myapplication.databinding.FragmentCompanyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class company_profile : Fragment() {

    private var _binding: FragmentCompanyProfileBinding? = null
    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyProfileBinding.inflate(inflater, container, false)

        setupSpinner()
        showLoading()
        fetchDataFromFirebase()

        binding.logout.setOnClickListener { logout() }
        binding.save.setOnClickListener { updateDataInFirebase() }

        return binding.root
    }

    private fun setupSpinner() {
        val disabilityArray = resources.getStringArray(R.array.Company_Array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, disabilityArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.IndustryTypespinner.adapter = adapter
    }

    private fun fetchDataFromFirebase() {
        val userId = firebaseAuth.currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            showContent()
            return
        }

        firestore.collection("companyData").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.toObject(CompanyData::class.java)
                    updateUIWithData(data)
                } else {
                    showContent()
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                showContent()
                Toast.makeText(context, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUIWithData(data: CompanyData?) {
        binding.CompanyName.setText(data?.companyName)
        binding.Email.setText(data?.email)
        binding.phone.setText(data?.phone)
        binding.Location.setText(data?.location)
        binding.URL.setText(data?.websiteUrl)
        binding.Disc.setText(data?.description)

        val spinnerPosition = (binding.IndustryTypespinner.adapter as ArrayAdapter<String>)
            .getPosition(data?.industryType)
        binding.IndustryTypespinner.setSelection(spinnerPosition)

        showContent()
    }

    private fun updateDataInFirebase() {
        showLoading()

        val updatedData = CompanyData(
            companyName = binding.CompanyName.text.toString(),
            email = binding.Email.text.toString(),
            phone = binding.phone.text.toString(),
            location = binding.Location.text.toString(),
            description = binding.Disc.text.toString(),
            websiteUrl = binding.URL.text.toString(),
            industryType = binding.IndustryTypespinner.selectedItem?.toString() ?: ""
        )

        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("companyData").document(userId)
            .set(updatedData)
            .addOnSuccessListener {
                showContent()
                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                showContent()
                Toast.makeText(context, "Error updating data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.theform.visibility = View.VISIBLE
        setFormEnabled(true)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.theform.visibility = View.GONE
        setFormEnabled(false)
    }

    private fun setFormEnabled(enabled: Boolean) {
        binding.CompanyName.isEnabled = enabled
        binding.Email.isEnabled = enabled
        binding.phone.isEnabled = enabled
        binding.Location.isEnabled = enabled
        binding.URL.isEnabled = enabled
        binding.Disc.isEnabled = enabled
        binding.IndustryTypespinner.isEnabled = enabled
    }

    private fun logout() {
        firebaseAuth.signOut()
        val sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
