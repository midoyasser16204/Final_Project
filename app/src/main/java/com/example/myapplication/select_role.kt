package com.example.myapplication

import android.content.Intent
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
//           findNavController().navigate(R.id.action_select_role_to_company_information)
            val intent = Intent(requireContext(), CompanyActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.cardJobSeeker.setOnClickListener {
//            findNavController().navigate(R.id.from_role_to_jopSekerInformation)
            val intent = Intent(requireContext(), DisabilityActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }


}