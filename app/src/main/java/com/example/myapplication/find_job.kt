package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.JobData
import com.example.myapplication.databinding.FragmentFindJobBinding
import com.google.firebase.firestore.FirebaseFirestore

class FindJobFragment : Fragment() {

    private lateinit var binding: FragmentFindJobBinding
    private lateinit var jobAdapter: JobAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindJobBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        // Initialize adapter with empty data and no click actions
        jobAdapter = JobAdapter(emptyList())

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
                    jobAdapter.updateData(jobs)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load jobs", Toast.LENGTH_SHORT).show()
            }
    }
}
