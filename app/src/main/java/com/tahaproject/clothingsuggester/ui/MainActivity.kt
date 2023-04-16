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


class MainActivity : AppCompatActivity(), WeatherView {

    private lateinit var presenter: WeatherPresenter
    private lateinit var apiRequest: ApiRequest
    private lateinit var weatherData: WeatherData
    private lateinit var requiredLocation: Location
    private val apiKey = BuildConfig.apiKey
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        apiRequest = ApiRequest()
        presenter = WeatherPresenterImpl(
            this,
            WeatherModelImpl(
                apiRequest, apiKey
            )
        )
        requestLocation()
//        (presenter as WeatherPresenterImpl).getCurrentWeatherData(requiredLocation.lat, requiredLocation.lon)
        weatherData = FakeDataGenerator.generateWeatherData()


        // TODO: make the xml
//        fetchWeatherButton.setOnClickListener {
//            val location = locationEditText.text.toString()
//            getCurrentWeather(location)
//        }
    }

    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
                location?.let {
                    requiredLocation =
                        Location(
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
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
