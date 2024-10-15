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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class FilePickerFragment : Fragment() {

    private lateinit var pdfPreview: ImageView
    private lateinit var profileImageView: ImageView
    private var selectedPdfUri: Uri? = null
    private var selectedImageUri: Uri? = null

    // Launchers for picking files
    private val pdfPickerLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedPdfUri = it
                showPdfPreview()
            }
        }

    private val imagePickerLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                showProfileImage()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file_picker, container, false)

        val pickPdfButton: Button = view.findViewById(R.id.pickPdfButton)
        val pickImageButton: Button = view.findViewById(R.id.pickImageButton)
        pdfPreview = view.findViewById(R.id.pdfPreview)
        profileImageView = view.findViewById(R.id.profileImageView)

        // Set click listeners for both buttons
        pickPdfButton.setOnClickListener { requestPermissionsForPdf() }
        pickImageButton.setOnClickListener { requestPermissionsForImage() }

        pdfPreview.setOnClickListener { openPdf() }

        return view
    }

    private fun showPdfPreview() {
        pdfPreview.visibility = View.VISIBLE
        Toast.makeText(requireContext(), "PDF selected!", Toast.LENGTH_SHORT).show()
    }

    private fun showProfileImage() {
        profileImageView.visibility = View.VISIBLE
        profileImageView.setImageURI(selectedImageUri)
        Toast.makeText(requireContext(), "Profile picture selected!", Toast.LENGTH_SHORT).show()
    }

    private fun openPdf() {
        selectedPdfUri?.let { uri ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            startActivity(intent)
        } ?: Toast.makeText(requireContext(), "No PDF selected", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermissionsForPdf() {
        requestPermissionsWithLauncher("application/pdf", pdfPickerLauncher)
    }

    private fun requestPermissionsForImage() {
        requestPermissionsWithLauncher("image/*", imagePickerLauncher)
    }

    private fun requestPermissionsWithLauncher(mimeType: String, launcher: ActivityResultLauncher<String>) {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }) {
            launcher.launch(mimeType)  // Launch the file picker directly
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // Permission granted; nothing more needed here since pickers are launched on button clicks
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
