package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.FragmentJobSeekerCardDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class job_seeker_card_detail : Fragment() {

    private lateinit var binding: FragmentJobSeekerCardDetailBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobSeekerCardDetailBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        // Get the user ID passed from the previous fragment
        val userId = arguments?.getString("userId")

        if (userId != null) {
            fetchDisabilityData(userId)
        } else {
            Toast.makeText(context, "No user ID found", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun fetchDisabilityData(userId: String) {
        firestore.collection("disabilityData").document(userId).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val disabilityData = documentSnapshot.toObject(DisabilityData::class.java)
                if (disabilityData != null) {
                    // Log the data to ensure it's being retrieved correctly
                    Log.d("DisabilityData", "Data fetched: $disabilityData")

                    // Display data in the UI
                    binding.Name.text = disabilityData.name
                    binding.age.text = disabilityData.age.toString()
                    binding.phone.text = disabilityData.phone
                    binding.Email.text = disabilityData.email
                    binding.skill.text = disabilityData.skill
                    binding.Address.text = disabilityData.address
                    binding.disability.text = disabilityData.disability

                    // Load profile image using Glide or any image loading library
                    disabilityData.profileImageUrl?.let { imageUrl ->
                        Glide.with(this)
                            .load(imageUrl)
                            .into(binding.profileImage)
                    }

                    // Handle PDF URL
                    disabilityData.pdfUrl?.let { pdfUrl ->
                        binding.pdfPreview.setOnClickListener {
                            openPdf(pdfUrl) // Modify openPdf to accept a URL
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to map data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to fetch data: ${exception.message}", Toast.LENGTH_SHORT).show()
            Log.e("DisabilityData", "Error fetching data", exception)
        }
    }

    private fun openPdf(pdfUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(pdfUrl), "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }


}