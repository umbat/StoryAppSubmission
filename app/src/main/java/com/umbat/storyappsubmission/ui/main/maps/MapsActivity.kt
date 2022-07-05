package com.umbat.storyappsubmission.ui.main.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.umbat.storyappsubmission.R
import com.umbat.storyappsubmission.databinding.ActivityMapsBinding
import com.umbat.storyappsubmission.model.StoryResponseItem
import com.umbat.storyappsubmission.ui.ViewModelFactory
import com.umbat.storyappsubmission.ui.registration.welcome.WelcomeActivity

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var storiesWithLocation: List<StoryResponseItem> = emptyList()
    private var token = ""

    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var viewModelFactory: ViewModelFactory
    private val mapsViewModel: MapsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
        mapsViewModel.getAllStoriesResponse.observe(this) { response ->
            if (response != null) {
                storiesWithLocation = response.listStory
                mapFragment.getMapAsync(this)
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

//        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(dicodingSpace)
//                .title("Dicoding Space")
//                .snippet("Batik Kumeli No.50")
//        )
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

        getMyLocation()
        setupLocationFromStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setupLocationFromStories() {
        if (storiesWithLocation.isEmpty()) return

        storiesWithLocation.forEach { story ->
            val location = LatLng(story.lat, story.lon)
            val address = getAddressName(location)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(address)
            )

            boundsBuilder.include(location)
            marker?.showInfoWindow()
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))
    }

    private fun getAddressName(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(this)
            val allAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (allAddress.isEmpty()) getString(R.string.empty_address) else allAddress[0].getAddressLine(
                0
            )
        } catch (e: Exception) {
            getString(R.string.empty_address)
        }
    }

    private fun setupViewModel() {
        viewModelFactory = ViewModelFactory.getInstance(this)

        showLoading()
        mapsViewModel.loadState().observe(this) {
            token = it.token
            if (!it.isLogin) {
                intentActivity()
            } else {
                getStoriesWithLocation(token)
            }
        }
        showToast()
    }

    private fun showLoading() {
        mapsViewModel.showLoading.observe(this@MapsActivity) {
            binding.progressBarMaps.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        mapsViewModel.toastText.observe(this@MapsActivity) { toastText ->
            Toast.makeText(
                this@MapsActivity, toastText, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun intentActivity() {
        startActivity(Intent(this@MapsActivity, WelcomeActivity::class.java))
    }

    private fun getStoriesWithLocation(token: String) {
        mapsViewModel.getStoriesWithLocation(token)
    }
}