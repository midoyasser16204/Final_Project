package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.model.DisabilityData
import com.example.myapplication.databinding.FragmentJopSeekerInformationBinding
import com.example.myapplication.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class jop_seeker_information : Fragment() {

    lateinit var binding: FragmentJopSeekerInformationBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by activityViewModels()

    private var selectedPdfUri: Uri? = null
    private var selectedImageUri: Uri? = null // إضافة URI للصورة

    private val pdfPickerLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedPdfUri = it
                uploadPdf(it, createDisabilityData()) // رفع الـ PDF عند تحديده
                showPdfPreview() // إظهار المعاينة
            }
        }

    private val imagePickerLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.profileImage.setImageURI(it) // عرض الصورة في ImageView
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJopSeekerInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDisabilitySpinner()
        setupButtons()

        binding.save.setOnClickListener { saveUserData()


        }
        binding.profileImage.setOnClickListener { imagePickerLauncher.launch("image/*") } // اختيار صورة عند الضغط على ImageView
    }

    private fun setupDisabilitySpinner() {
        val disabilityArray = resources.getStringArray(R.array.Disability_Array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, disabilityArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.disabilitySpinner.adapter = adapter
    }

    private fun setupButtons() {
        binding.tvUploadFile.setOnClickListener { requestPermissionsForPdf() }
        binding.pdfPreview.setOnClickListener { openPdf() }
    }

    private fun saveUserData() {
        val disabilityData = createDisabilityData() // إنشاء بيانات الإعاقة

        // رفع الصورة الشخصية إذا كانت موجودة
        selectedImageUri?.let {
            uploadProfileImage(it, disabilityData)
        } ?: run {
            if (selectedPdfUri != null) {
                uploadPdf(selectedPdfUri!!, disabilityData) // رفع الـ PDF إذا كان محدد
            } else {
                saveDisabilityData(disabilityData, pdfUrl = null) // حفظ البيانات بدون PDF
            }
        }
    }

    private fun createDisabilityData(): DisabilityData {
        return DisabilityData(
            name = binding.Name.text.toString(),
            age = binding.age.text.toString().toIntOrNull() ?: 0,
            phone = binding.phone.text.toString(),
            email = binding.Email.text.toString(),
            skill = binding.skill.text.toString(),
            address = binding.address.text.toString(),
            disability = binding.disabilitySpinner.selectedItem.toString()
        )
    }

    private fun uploadPdf(pdfUri: Uri, disabilityData: DisabilityData) {
        val uid = userViewModel.Uid.toString()
        val storageRef = storage.reference.child("user_cvs/$uid/${UUID.randomUUID()}.pdf")

        storageRef.putFile(pdfUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveDisabilityData(disabilityData, uri.toString()) // حفظ بيانات الإعاقة مع رابط الـ PDF
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "فشل في رفع الـ PDF", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProfileImage(imageUri: Uri, disabilityData: DisabilityData) {
        val uid = userViewModel.Uid.toString()
        val storageRef = storage.reference.child("profile_images/$uid/profile.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // هنا ممكن تخزن الـdownloadUri في Firestore مع بيانات المستخدم
                    saveDisabilityData(disabilityData, pdfUrl = null)
                    Toast.makeText(requireContext(), "تم رفع صورة البروفايل!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "فشل في رفع الصورة", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDisabilityData(disabilityData: DisabilityData, pdfUrl: String?) {
        val uid = auth.currentUser?.uid
        val userDoc = firestore.collection("disabilityData").document(uid.toString())

        val dataMap = disabilityData.toMap().apply {
            pdfUrl?.let { put("pdfUrl", it) }
        }

        userDoc.set(dataMap)
            .addOnSuccessListener {
                Toast.makeText(context, "تم حفظ البيانات بنجاح", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener {
                Toast.makeText(context, "فشل في حفظ البيانات", Toast.LENGTH_SHORT).show()
            }
    }

    fun DisabilityData.toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "name" to name,
            "age" to age,
            "phone" to phone,
            "email" to email,
            "skill" to skill,
            "disability" to disability,
            "address" to address
        )
    }

    private fun showPdfPreview() {
        binding.pdfPreview.visibility = View.VISIBLE
        Toast.makeText(requireContext(), "تم اختيار الـ PDF!", Toast.LENGTH_SHORT).show()
    }

    private fun openPdf() {
        selectedPdfUri?.let { uri ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            startActivity(intent)
        } ?: Toast.makeText(requireContext(), "لا يوجد PDF محدد", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermissionsForPdf() {
        requestPermissionsWithLauncher("application/pdf", pdfPickerLauncher)
    }

    private fun requestPermissionsWithLauncher(mimeType: String, launcher: ActivityResultLauncher<String>) {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }) {
            launcher.launch(mimeType)
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // Permissions granted; no further action required
        } else {
            Toast.makeText(requireContext(), "تم رفض الإذن", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
