package com.tahaproject.clothingsuggester.data.models.response

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("city") val city: City,
    @SerializedName("list") val forecasts: List<Forecast>
)










