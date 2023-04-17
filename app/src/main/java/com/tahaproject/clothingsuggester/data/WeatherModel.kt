package com.tahaproject.clothingsuggester.data

import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.data.models.response.WeatherData

interface WeatherModel {
    fun fetchCurrentWeatherData(
        location: Location,
        callback: (WeatherData) -> Unit,
        errorCallback: (String) -> Unit
    )
}
