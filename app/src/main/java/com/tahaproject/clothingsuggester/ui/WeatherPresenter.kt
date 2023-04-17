package com.tahaproject.clothingsuggester.ui

import com.tahaproject.clothingsuggester.data.WeatherModel
import com.tahaproject.clothingsuggester.data.models.requests.Location

class WeatherPresenter(private val view: IWeatherView, private val model: WeatherModel) : IWeatherPresenter {


    override fun getCurrentWeatherData(location: Location) {
        model.fetchCurrentWeatherData(location, { weatherData ->
            view.showCurrentWeatherData(weatherData)
        }, { errorMessage ->
            view.showError(errorMessage)
        })
    }

    override fun onDestroy() {
        // Clean up resources, if necessary
    }
}
