package com.tahaproject.clothingsuggester.data.models.response

import com.google.gson.annotations.SerializedName

data class Forecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    @SerializedName("dt_txt") val dtTxt: String
)