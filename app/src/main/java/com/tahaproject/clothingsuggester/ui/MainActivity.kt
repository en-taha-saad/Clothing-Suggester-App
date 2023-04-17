package com.tahaproject.clothingsuggester.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tahaproject.clothingsuggester.BuildConfig
import com.tahaproject.clothingsuggester.R
import com.tahaproject.clothingsuggester.data.ApiRequest
import com.tahaproject.clothingsuggester.data.FakeDataGenerator
import com.tahaproject.clothingsuggester.data.WeatherModelImpl
import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.data.models.response.WeatherData


class MainActivity : AppCompatActivity(), IWeatherView {

    private lateinit var presenter: WeatherPresenter
    private lateinit var apiRequest: ApiRequest
    private lateinit var weatherData: WeatherData
    private lateinit var requiredLocation: Location
    private val apiKey = BuildConfig.apiKey
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apiRequest = ApiRequest()
        presenter = WeatherPresenter(
            this,
            WeatherModelImpl(
                apiRequest, apiKey
            )
        )
        weatherData = FakeDataGenerator.generateWeatherData()
//        presenter.getCurrentWeatherData(requiredLocation)

        // Initialize FusedLocationProviderClient for Location Services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationPermission()
        // TODO: make the xml
        // setUp()
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


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
                location?.let {
                    presenter.getCurrentWeatherData(Location(it.latitude.toString(), it.longitude.toString()))
                } ?: showError("Failed to get location")
            }
            return
        }
    }

    override fun showCurrentWeatherData(weatherData: WeatherData) {
//        temperatureTextView.text = "Temperature: ${weatherData.temperature}"
//        descriptionTextView.text = "Description: ${weatherData.description}"
    }

    override fun showError(errorMessage: String) {
//        errorTextView.text = errorMessage
    }
}
