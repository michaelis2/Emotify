package com.example.emotify.View

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.emotify.R
import com.example.emotify.ViewModel.MainViewModel
import com.example.emotify.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MainViewModel by viewModels()
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (savedInstanceState == null) {
            replaceFragment(Home())  // Default fragment on app launch
        }

        // Set up BottomNavigationView listener to switch between fragments
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> {
                    replaceFragment(Home())
                    true
                }
                R.id.Camera -> {
                    replaceFragment(Camera())
                    true
                }
                R.id.Settings -> {
                    replaceFragment(Settings())
                    true
                }
                else -> false
            }
        }
        requestLocationPermission()
    }
    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getUserCountryCode()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getUserCountryCode()
        }
    }

    // ✅ Get user's country code using location
    private fun getUserCountryCode() {
        Log.d("MainActivity", "getUserCountryCode() called")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    fetchCountryCode(location.latitude, location.longitude)
                } else {
                    requestNewLocation()
                }
            }
        }
    }

    // ✅ Fetch country code using the Geocoder API
    private fun fetchCountryCode(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 14+ (API 33+)
            geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                if (!addresses.isNullOrEmpty()) {
                    val countryCode = addresses[0].countryCode
                    viewModel.setCountryCode(countryCode)
                    Log.d("MainActivity", "Country code set to: $countryCode")
                }
            }
        } else {
            // For older Android versions
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val countryCode = addresses[0].countryCode
                    viewModel.setCountryCode(countryCode)
                    Log.d("MainActivity", "Country code set to: $countryCode")

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Request fresh location update if lastLocation is null
    private fun requestNewLocation() {
        Log.d("MainActivity", "requestnewloc() called")
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    fetchCountryCode(location.latitude, location.longitude)
                }
                fusedLocationClient.removeLocationUpdates(this) // Stop updates once we get a location
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    // Function to replace the fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
    // Request location permission


}