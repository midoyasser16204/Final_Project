package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSelectRoleBinding


class select_role : Fragment() {

lateinit var binding: FragmentSelectRoleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSelectRoleBinding.inflate(inflater,container,false)
        return binding.root
    }
    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardCompany.setOnClickListener {
         //  findNavController().navigate(R.id.action_selection_to_employerHome)
        }
        binding.cardJobSeeker.setOnClickListener {
            findNavController().navigate(R.id.action_select_role_to_jop_seeker_information)
        }
    }


}