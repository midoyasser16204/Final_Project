package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.FragmentJobSeekerCardsBinding
import com.google.firebase.firestore.FirebaseFirestore

class job_seeker_cards : Fragment() {

    private lateinit var binding: FragmentJobSeekerCardsBinding
    private lateinit var disabilityAdapter: DisabilityAdapter
    private var disabilityList = mutableListOf<DisabilityData>()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobSeekerCardsBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        disabilityAdapter = DisabilityAdapter(disabilityList) { disabilityData ->
            showDetail(disabilityData)
        }

        binding.jobSeekerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = disabilityAdapter
        }

        loadJobSeekersData()

        return binding.root
    }

    private fun loadJobSeekersData() {
        firestore.collection("disabilityData").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                }
                else {
                    val newList = mutableListOf<DisabilityData>()
                    for (document in documents) {
                        val data = document.toObject(DisabilityData::class.java)
                        data.id = document.id // Set the document ID
                        newList.add(data)
                    }
                    disabilityAdapter.updateData(newList)
                }
            }
            .addOnFailureListener { exception ->
            }
    }


    private fun showDetail(disabilityData: DisabilityData) {
        val bundle = Bundle().apply {
            putString("userId", disabilityData.id) // Pass userId to Detail fragment
        }
        findNavController().navigate(R.id.action_nav_job_seekers_to_job_seeker_card_detail, bundle)
    }

}
