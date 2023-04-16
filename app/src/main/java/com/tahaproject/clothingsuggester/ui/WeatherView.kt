package com.tahaproject.clothingsuggester.ui

import com.tahaproject.clothingsuggester.data.models.response.WeatherData

interface WeatherView {
    fun showCurrentWeatherData(weatherData: WeatherData)
    fun showError(errorMessage: String)
}
