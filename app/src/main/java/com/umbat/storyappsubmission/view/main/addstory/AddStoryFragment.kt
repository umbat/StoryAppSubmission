package com.umbat.storyappsubmission.view.main.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.view.ViewModelFactory
import com.umbat.storyappsubmission.view.main.home.HomeFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.umbat.storyappsubmission.databinding.FragmentAddStoryBinding
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private val addStoryViewModel: AddStoryViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelFactory = ViewModelFactory.getInstance(requireContext())

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                 REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadStory() }

        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadStory() {
        showLoading()
        addStoryViewModel.loadState().observe(viewLifecycleOwner) {
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                uploadResponse(
                    it.token,
                    imageMultipart,
                    binding.edtDescStory.text.toString().toRequestBody("text/plain".toMediaType())
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.input_image),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun uploadResponse(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) {
        addStoryViewModel.uploadStory(token, file, description)
        addStoryViewModel.fileUploadResponse.observe(viewLifecycleOwner) {
            if (!it.error) {
                intentActivity()
            }
        }
        showToast()
    }

    private fun intentActivity() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_activity, HomeFragment())
        transaction.commit()
    }

    private fun showToast() {
        addStoryViewModel.toastText.observe(viewLifecycleOwner) { toastText ->
            Toast.makeText(
                requireContext(), toastText, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showLoading() {
        addStoryViewModel.showLoading.observe(viewLifecycleOwner) {
            binding.progressBarAddStory.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}