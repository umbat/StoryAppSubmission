package com.umbat.storyappsubmission.ui.main.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.ui.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.umbat.storyappsubmission.databinding.FragmentAddStoryBinding
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.random.Random

class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private val addStoryViewModel: AddStoryViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

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
        viewModelFactory = ViewModelFactory.getInstance(requireActivity())

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.ibLocation.setOnClickListener { getLocationNow() }
        binding.btnUpload.setOnClickListener { uploadStory() }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getLocationNow()
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getLocationNow()
            else -> {}
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocationNow() {
        if (
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val addressName = getAddressName(LatLng(location.latitude, location.longitude))
                    binding.etLocation.setText(addressName)
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.empty_location), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getAddressName(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(requireContext())
            val allAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (allAddress.isEmpty()) getString(R.string.empty_address) else allAddress[0].getAddressLine(
                0
            )
        } catch (e: Exception) {
            getString(R.string.empty_address)
        }
    }

    private fun addressToCoordinate(locationName: String): LatLng {
        return try {
            val randomLatitude = randomCoordinate()
            val randomLongitude = randomCoordinate()

            val geocoder = Geocoder(requireContext())
            val allLocation = geocoder.getFromLocationName(locationName, 1)
            if (allLocation.isEmpty()) {
                LatLng(randomLatitude, randomLongitude)
            } else {
                LatLng(allLocation[0].latitude, allLocation[0].longitude)
            }
        } catch (e: Exception) {
            LatLng(0.0, 0.0)
        }
    }

    private fun randomCoordinate(): Double {
        return Random.nextDouble(15.0, 100.0)
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
                val lat: RequestBody? = null
                val lon: RequestBody? = null
                uploadResponse(
                    it.token,
                    imageMultipart,
                    binding.edtDescStory.text.toString().toRequestBody("text/plain".toMediaType()),
                    lat.toString().toRequestBody("text/plain".toMediaType()),
                    lon.toString().toRequestBody("text/plain".toMediaType())
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
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
        addStoryViewModel.uploadStory(token, file, description, lat, lon)
        addStoryViewModel.fileUploadResponse.observe(viewLifecycleOwner) {
            if (!it.error) {
                intentFragment()
            }
        }
        showToast()
    }

    private fun intentFragment() {
        findNavController().navigate(R.id.action_navigation_addStory_to_navigation_home)
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