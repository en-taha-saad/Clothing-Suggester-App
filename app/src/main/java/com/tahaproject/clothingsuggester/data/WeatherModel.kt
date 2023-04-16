package com.tahaproject.clothingsuggester.data

import com.tahaproject.clothingsuggester.data.models.response.WeatherData

interface WeatherModel {
    fun fetchCurrentWeatherData(
        lat: String,
        long: String,
        callback: (WeatherData) -> Unit,
        errorCallback: (String) -> Unit
    )
}
