package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.FragmentJobOfferBinding
import com.google.firebase.firestore.FirebaseFirestore

class JobOfferFragment : Fragment() {

    private lateinit var binding: FragmentJobOfferBinding
    private lateinit var jobAdapter: JobAdapter // Use JobAdapter for jobs
    private var jobList = mutableListOf<JobData>() // This will hold the job data
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobOfferBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        jobAdapter = JobAdapter(jobList, { jobData ->
            showDetail(jobData)
        }, { jobData ->
            deleteJob(jobData)
        })

        binding.jobSeekerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter // Set the correct adapter
        }

        binding.addjob.setOnClickListener {
            navigateToAddJobScreen()
        }

        loadJobData() // Load jobs instead of job seekers

        return binding.root
    }

    private fun loadJobData() {
        firestore.collection("jobData").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "No jobs available", Toast.LENGTH_SHORT).show()
                } else {
                    val newJobList = mutableListOf<JobData>() // Create a new list
                    for (document in documents) {
                        val jobData = document.toObject(JobData::class.java)
                        jobData.id = document.id // Assuming JobData has an id property
                        newJobList.add(jobData)
                    }
                    jobAdapter.updateData(newJobList) // Update the adapter with new data
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to load jobs", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showDetail(jobData: JobData) {
        val bundle = Bundle().apply {
            putString("jobId", jobData.id) // Pass jobId to Detail fragment
        }
        findNavController().navigate(R.id.action_nav_job_seekers_to_job_seeker_card_detail, bundle)
    }

    private fun navigateToAddJobScreen() {
        findNavController().navigate(R.id.action_jobOfferFragment_to_add_job) // No need to pass companyId
    }

    private fun deleteJob(jobData: JobData) {
        Log.d("JobOfferFragment", "Deleting job with ID: ${jobData.id}")
        firestore.collection("jobData").document(jobData.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Job deleted successfully", Toast.LENGTH_SHORT).show()
                loadJobData() // Refresh the list after deletion
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to delete job", Toast.LENGTH_SHORT).show()
            }
    }

}
