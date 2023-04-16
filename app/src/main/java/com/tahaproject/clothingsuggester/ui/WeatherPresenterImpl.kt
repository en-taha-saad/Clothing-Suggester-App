package com.tahaproject.clothingsuggester.ui

import com.tahaproject.clothingsuggester.data.WeatherModel

class WeatherPresenterImpl(private val view: WeatherView, private val model: WeatherModel) : WeatherPresenter {


    override fun getCurrentWeatherData(lat: String, lon: String) {
        model.fetchCurrentWeatherData(lat, lon, { weatherData ->
            view.showCurrentWeatherData(weatherData)
        }, { errorMessage ->
            view.showError(errorMessage)
        })
    }

    override fun onDestroy() {
        // Clean up resources, if necessary
    }
}
