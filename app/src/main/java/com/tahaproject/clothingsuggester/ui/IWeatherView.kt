package com.tahaproject.clothingsuggester.ui

import com.tahaproject.clothingsuggester.data.models.requests.Outfit
import com.tahaproject.clothingsuggester.data.models.response.WeatherData

interface IWeatherView {
    fun showCurrentWeatherData(weatherData: WeatherData)
    fun showError(errorMessage: String)
    fun showSuggestedOutfit(outfit: Outfit)

    fun showLoading()

    fun hideLoading()
}
