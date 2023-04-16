package com.tahaproject.clothingsuggester.ui

interface WeatherPresenter {
    fun getCurrentWeatherData(lat: String, lon: String)
    fun onDestroy()
}
