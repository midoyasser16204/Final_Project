package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.JobData
import com.example.myapplication.data.model.Model
import com.example.myapplication.databinding.FragmentAddJobBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class add_job : Fragment() {

    private lateinit var binding: FragmentAddJobBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddJobBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        binding.save.setOnClickListener {
            addJobToFirestore()
        }

        return binding.root
    }

    private fun addJobToFirestore() {
        val jobTitle = binding.Jobtitle.text.toString().trim()
        val jobDescription = binding.Jobdesc.text.toString().trim()
        val salary = binding.Salary.text.toString().trim()
        val location = binding.Location.text.toString().trim()

        // Get the company ID from FirebaseAuth
        val companyId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (jobTitle.isNotEmpty() && jobDescription.isNotEmpty() && salary.isNotEmpty() && location.isNotEmpty() && companyId.isNotEmpty()) {
            // Create the JobData object with the companyId
            val jobData = JobData(
                id = "",
                jobtitle = jobTitle,
                jobdesc = jobDescription,
                salary = salary,
                location = location,
                companyId = companyId // Associate the job with the company
            )

            firestore.collection("jobData").add(jobData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Job added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to add job", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

}

