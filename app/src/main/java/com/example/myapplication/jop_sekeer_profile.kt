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
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.FragmentJopSekeerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class jop_sekeer_profile : Fragment() {
    private lateinit var binding: FragmentJopSekeerProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJopSekeerProfileBinding.inflate(inflater, container, false)

        // Show loading indicator
        showLoading()
        setupDisabilitySpinner()
        // Fetch data from Firebase and set to UI fields
        fetchDataFromFirebase()
        binding.logout.setOnClickListener {
            logout()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.setOnClickListener {
            updateDataInFirebase()
        }
    }

    private fun setupDisabilitySpinner() {
        val disabilityArray = resources.getStringArray(R.array.Disability_Array)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, disabilityArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.disabilitySpinner.adapter = adapter
    }

    private fun fetchDataFromFirebase() {
        // Example: assuming collection is "users" and document is the userId
        val userId =
            firebaseAuth.currentUser?.uid ?: ""// replace with dynamic user ID if needed

        firestore.collection("disabilityData").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.toObject(DisabilityData::class.java)


                    // Set data into UI fields
                    binding.Name.setText(data?.name)
                    binding.Email.setText(data?.email)
                    binding.phone.setText(data?.phone)
                    binding.age.setText(data?.age.toString())
                    binding.address.setText(data?.address)
                    binding.skill.setText(data?.skill)
                    val spinnerPosition =
                        (binding.disabilitySpinner.adapter as ArrayAdapter<String>)
                            .getPosition(data?.disability)
                    binding.disabilitySpinner.setSelection(spinnerPosition)


                    // Hide loading indicator
                    showContent()
                } else {
                    // Hide loading if no data found
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Error fetching data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateDataInFirebase() {
        // Show the loading indicator while updating
        showLoading()

        // Gather the updated data from the UI
        val updatedData = DisabilityData(
            name = binding.Name.text.toString(),
            email = binding.Email.text.toString(),
            phone = binding.phone.text.toString(),
            age = binding.age.text.toString().toIntOrNull() ?: 0,  // Handle invalid input
            address = binding.address.text.toString(),
            skill = binding.skill.text.toString(),
            disability = binding.disabilitySpinner.selectedItem.toString()
        )

        // Example: assuming collection is "disabilityData" and document is the userId
        val userId = firebaseAuth.currentUser?.uid ?: "" // Replace with dynamic user ID if needed

        // Update the data in Firestore
        firestore.collection("disabilityData").document(userId)
            .set(updatedData) // You can use update() if you want to only update specific fields
            .addOnSuccessListener {
                // Hide the loading indicator
                showContent()

                // Show success message
                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Hide the loading indicator
                showContent()

                // Handle the error
                Toast.makeText(
                    context,
                    "Error updating data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showContent() {
        // Hide the loading indicator and show the UI
        binding.progressBar.visibility = View.GONE
        binding.theform.visibility = View.VISIBLE
    }

    private fun showLoading() {
        // Show only the loading indicator and hide other UI components
        binding.progressBar.visibility = View.VISIBLE
        binding.theform.visibility = View.GONE
    }
    private fun logout() {
        // Sign out from Firebase
        firebaseAuth.signOut()

        // Clear SharedPreferences if needed
        val sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Navigate to MainActivity
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish() // Finish the current activity
    }


}