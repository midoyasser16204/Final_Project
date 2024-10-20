package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.CompanyData
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.FragmentCompanyInformationBinding
import com.example.myapplication.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class company_information : Fragment() {
    lateinit var binding: FragmentCompanyInformationBinding

    private lateinit var firestore: FirebaseFirestore
    lateinit var fireAuth: FirebaseAuth

    //    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        fireAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        binding = FragmentCompanyInformationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.setOnClickListener {
            val Companyname = binding.CompanyName.text.toString()
            val Companyphone = binding.phone.text.toString()
            val Companyemail = binding.Email.text.toString()
            val Companyaddress = binding.Location.text.toString()
            val companyWebsite = binding.URL.text.toString()
            val companyDescription = binding.Disc.text.toString()
            val companyInformation = CompanyData(
                companyName = Companyname,
                phone = Companyphone,
                email = Companyemail,
                location = Companyaddress,
                websiteUrl = companyWebsite,
                description = companyDescription
            )
            saveDisabilityData(companyInformation)


        }
        //        binding.save.setOnClickListener {
//            val name = binding.Name.text.toString()
//            val age = binding.age.text.toString().toIntOrNull() ?: 0 // Ensure age is an Int
//            val phone = binding.phone.text.toString()
//            val email = binding.Email.text.toString()
//            val skill = binding.skill.text.toString()
//            val address = binding.address.text.toString()
//            val selectedDisability = binding.disabilitySpinner.selectedItem.toString()
//
//            // Create the DisabilityData object
//            val disabilityData = DisabilityData(
//                name = name,
//                age = age,
//                phone = phone,
//                email = email,
//                skill = skill,
//                disability = selectedDisability,
//                address = address
//            )
//            saveDisabilityData(disabilityData)
//
//
//        }
//        val disabilityArray = resources.getStringArray(R.array.Company_Array)
//
//        // Create an ArrayAdapter
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, disabilityArray)
//
//        // Specify the layout for the dropdown items
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        // Set the adapter to the Spinner
//        binding.disabilitySpinner.adapter = adapter
//
//
//
//
//    }
    }

    fun saveDisabilityData(companyData: CompanyData) {
        val docRef =
            firestore.collection("comapnyData").document(fireAuth.currentUser?.uid.toString())

        docRef.set(companyData)
            .addOnSuccessListener {
                Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }

}