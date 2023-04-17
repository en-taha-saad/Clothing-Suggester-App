package com.tahaproject.clothingsuggester.ui

import com.tahaproject.clothingsuggester.data.models.requests.Location

interface IWeatherPresenter {
    fun getCurrentWeatherData(location: Location)
    fun onDestroy()
}
