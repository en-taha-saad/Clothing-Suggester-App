package com.tahaproject.clothingsuggester.data.remote

import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.data.models.response.WeatherData

interface WeatherModel {
    fun fetchCurrentWeatherData(
        location: Location,
        callback: (WeatherData) -> Unit,
        errorCallback: (String) -> Unit
    )
}
