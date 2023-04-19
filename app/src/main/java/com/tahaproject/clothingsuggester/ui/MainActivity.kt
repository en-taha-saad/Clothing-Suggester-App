package com.tahaproject.clothingsuggester.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tahaproject.clothingsuggester.BuildConfig
import com.tahaproject.clothingsuggester.data.local.LocalDataImpl
import com.tahaproject.clothingsuggester.data.remote.ApiRequest
import com.tahaproject.clothingsuggester.data.remote.WeatherModelImpl
import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.data.models.requests.Outfit
import com.tahaproject.clothingsuggester.data.models.response.WeatherData
import com.tahaproject.clothingsuggester.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), IWeatherView {
    private lateinit var binding: ActivityMainBinding
    private val apiKey = BuildConfig.apiKey
    private val apiRequest by lazy { ApiRequest() }
    private val weatherModelImpl by lazy { WeatherModelImpl(apiRequest, apiKey) }
    private val sharedPref by lazy { getSharedPreferences("ClothesSuggesterSharedPref", MODE_PRIVATE) }
    private val localDataImpl by lazy { LocalDataImpl(sharedPref) }
    private val presenter by lazy { WeatherPresenter(this, weatherModelImpl, localDataImpl) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun ImageView.loadImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
    }

    override fun showCurrentWeatherData(weatherData: WeatherData) {
        val forecast = weatherData.forecasts.first()

        runOnUiThread {
            binding.apply {
                textViewTemperature.text =
                    "${forecast.main.tempMin.toInt()} °C/ ${forecast.main.tempMax.toInt()} °C"
                textViewState.text = forecast.weather.first().description
                imageViewIcon.loadImageFromUrl("https://openweathermap.org/img/wn/" + forecast.weather.first().icon + ".png")
            }

        }

        updateUI(weatherData)
    }

    override fun showError(errorMessage: String) {
        runOnUiThread {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

    }

    override fun showSuggestedOutfit(outfit: Outfit) {
        Log.i("Outfit", outfit.toString())
        runOnUiThread {
            binding.imageViewOutfit.setImageResource(outfit.imageResource)
        }
    }

    override fun showLoading() {
        runOnUiThread {
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        runOnUiThread {
            binding.progressBar.visibility = View.GONE
        }
    }

}
