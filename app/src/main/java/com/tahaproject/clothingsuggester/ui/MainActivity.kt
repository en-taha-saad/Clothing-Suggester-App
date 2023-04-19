package com.tahaproject.clothingsuggester.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tahaproject.clothingsuggester.BuildConfig
import com.tahaproject.clothingsuggester.data.remote.ApiRequest
import com.tahaproject.clothingsuggester.data.remote.WeatherModelImpl
import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.data.models.response.WeatherData
import com.tahaproject.clothingsuggester.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), IWeatherView {
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: WeatherPresenter
    private lateinit var apiRequest: ApiRequest
    private lateinit var weatherModelImpl: WeatherModelImpl

    private lateinit var requiredLocation: Location
    private val apiKey = BuildConfig.apiKey
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiRequest = ApiRequest()
        weatherModelImpl = WeatherModelImpl(apiRequest, apiKey)
        presenter = WeatherPresenter(this, weatherModelImpl)

        // Initialize FusedLocationProviderClient for Location Services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                showError("Location permission denied")
            }
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val address = addresses?.firstOrNull()
        return address?.getAddressLine(0) ?: "Unknown Location"
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            location?.let {
//                val address = getAddressFromLocation(it.latitude, it.longitude)
                presenter.getCurrentWeatherData(Location(it.latitude.toString(), it.longitude.toString()))
            } ?: showError("Failed to get location")
        }

    }


    private fun updateUI(weatherData: WeatherData) {
        if (weatherData.forecasts.isEmpty()) {
            binding.progressBar.visibility = View.GONE
//            binding.locationTextView.visibility = View.VISIBLE
//            binding.locationTextView.text = "No weather data available"
            showError("No forecasts available")
            return
        }
        val forecast = weatherData.forecasts.first()
        if (forecast.weather.isEmpty()) {
            binding.progressBar.visibility = View.GONE
//            binding.locationTextView.visibility = View.VISIBLE
//            binding.locationTextView.text = "No weather data available"
            showError("No weather data available")
            return
        }
        val weather = forecast.weather.first()
        val main = forecast.main

        runOnUiThread {
            binding.progressBar.visibility = View.GONE
//            binding.locationTextView.visibility = View.VISIBLE
//            binding.locationTextView.text = weather.toString()
        }
    }


    override fun showCurrentWeatherData(weatherData: WeatherData) {
        updateUI(weatherData)
    }

    override fun showError(errorMessage: String) {
        // errorTextView.text = errorMessage
        // make a toast
        runOnUiThread {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

    }
}
