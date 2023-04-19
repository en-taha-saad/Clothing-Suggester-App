package com.tahaproject.clothingsuggester.ui

import com.tahaproject.clothingsuggester.data.local.LocalDataImpl
import com.tahaproject.clothingsuggester.data.remote.WeatherModel
import com.tahaproject.clothingsuggester.data.models.requests.Location


class WeatherPresenter(
    private val view: IWeatherView,
    private val model: WeatherModel,
    private val localDataImpl: LocalDataImpl
) : IWeatherPresenter {


    override fun getCurrentWeatherData(location: Location) {
        model.fetchCurrentWeatherData(location, { weatherData ->
            view.hideLoading()
            getSuggestedOutfit(weatherData.forecasts.first().main.temp)
            view.showCurrentWeatherData(weatherData)
        }, { errorMessage ->
            view.hideLoading()
            view.showError(errorMessage)
        })
    }


    private fun getSuggestedOutfit(temperature: Double) {
        val suggestedOutfit = localDataImpl.getSuggestedOutfit(temperature)
        if (suggestedOutfit == null) {
            view.showError("No outfit available")
        } else {
            view.showSuggestedOutfit(suggestedOutfit)
        }
    }
}
