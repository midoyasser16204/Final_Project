package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.FragmentFindJobBinding
import com.google.firebase.firestore.FirebaseFirestore

class FindJobFragment : Fragment() {

    private lateinit var binding: FragmentFindJobBinding
    private lateinit var jobAdapter: JobDetailAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindJobBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        // Initialize adapter with an empty list and click handling
        jobAdapter = JobDetailAdapter(emptyList()) { jobData ->
            navigateToJobDetail(jobData.id) // Pass the job ID to the navigation function
        }

        binding.jobSeekerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }

        loadJobData()
        return binding.root
    }

    private fun loadJobData() {
        firestore.collection("jobData").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "No jobs available", Toast.LENGTH_SHORT).show()
                } else {
                    val jobs = documents.map {
                        it.toObject(JobData::class.java).apply { id = it.id }
                    }
                    jobAdapter.updateData(jobs) // Update the adapter with job data
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load jobs", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToJobDetail(jobId: String) {
        // Create a Bundle to pass the job ID
        val bundle = Bundle().apply {
            putString("jobId", jobId)
        }
        findNavController().navigate(R.id.action_find_job_to_job_detail, bundle) // Navigate with the Bundle
    }
}
