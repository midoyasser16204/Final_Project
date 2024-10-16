package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentCompanyHomeScreenBinding

class company_home_screen : Fragment() {
    lateinit var binding: FragmentCompanyHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompanyHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to CompanyHome when the button is clicked
        binding.JobSeekercard.setOnClickListener {
//            findNavController().navigate(R.id.action_companyHomeScreen3_to_companyHome)
        }
    }
}
