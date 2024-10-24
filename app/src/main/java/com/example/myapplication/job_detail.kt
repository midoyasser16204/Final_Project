package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.FragmentJobCardBinding
import com.google.firebase.firestore.FirebaseFirestore

class job_detail : Fragment() {

    private lateinit var binding: FragmentJobCardBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobCardBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        // Get the job ID passed from the previous fragment
        val jobId = arguments?.getString("jobId")

        if (jobId != null) {
            fetchJobData(jobId)
        } else {
            Toast.makeText(context, "No job ID found", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // Fetch job data from Firestore using the provided job ID
    private fun fetchJobData(jobId: String) {
        firestore.collection("jobData").document(jobId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val jobData = documentSnapshot.toObject(JobData::class.java)
                    if (jobData != null) {
                        Log.d("JobData", "Data fetched: $jobData")

                        // Bind the data to the UI elements
                        binding.jobTitle.text = jobData.jobtitle
                        binding.jobDesc.text = jobData.jobdesc
                        binding.salary.text = jobData.salary
                        binding.location.text = jobData.location
                        binding.contact.text = jobData.contact

                        // Load company logo or other images if needed
                        // Uncomment and use if your JobData model includes a logo URL
                        // jobData.companyLogoUrl?.let { logoUrl ->
                        //     Glide.with(this).load(logoUrl).into(binding.companyLogo)
                        // }
                    } else {
                        Toast.makeText(context, "Failed to map data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No job data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch data: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("JobData", "Error fetching data", exception)
            }
    }
}
