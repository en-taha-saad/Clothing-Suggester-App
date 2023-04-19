package com.tahaproject.clothingsuggester.data.remote

import android.util.Log
import com.google.gson.Gson
import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.data.models.response.WeatherData
import okhttp3.*
import java.io.IOException

class WeatherModelImpl(private val apiRequest: ApiRequest, private val apiKey: String) : WeatherModel {
    val gson = Gson()
    override fun fetchCurrentWeatherData(
        location: Location,
        callback: (WeatherData) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        return apiRequest.getCurrentWeather(location, apiKey, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e.message ?: "Error fetching weather data")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseJson = response.body?.string()
                Log.i("WeatherModelImpl", responseJson.toString())
                responseJson.let { responseBody ->
                    val weatherData = gson.fromJson(responseBody, WeatherData::class.java)
                    weatherData?.let(callback) ?: errorCallback("Error parsing weather data")
                } ?: errorCallback("Error fetching weather data")
            }
        })
    }
}
