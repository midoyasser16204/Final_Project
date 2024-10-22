package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.FragmentJobOfferBinding
import com.google.firebase.firestore.FirebaseFirestore

class JobOfferFragment : Fragment() {

    private lateinit var binding: FragmentJobOfferBinding
    private lateinit var jobAdapter: JobAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobOfferBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        jobAdapter = JobAdapter(
            emptyList(),
            onDetailClick = { jobData -> showDetail(jobData) },
            onDeleteClick = { jobData -> deleteJob(jobData) }
        )

        binding.jobSeekerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }

        binding.addjob.setOnClickListener { navigateToAddJobScreen() }
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
                    jobAdapter.updateData(jobs)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load jobs", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDetail(jobData: JobData) {
        val bundle = Bundle().apply { putString("jobId", jobData.id) }
        findNavController().navigate(R.id.action_nav_job_seekers_to_job_seeker_card_detail, bundle)
    }

    private fun navigateToAddJobScreen() {
        findNavController().navigate(R.id.action_jobOfferFragment_to_add_job)
    }

    private fun deleteJob(jobData: JobData) {
        firestore.collection("jobData").document(jobData.id).delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Job deleted", Toast.LENGTH_SHORT).show()
                loadJobData()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to delete job", Toast.LENGTH_SHORT).show()
            }
    }
}
