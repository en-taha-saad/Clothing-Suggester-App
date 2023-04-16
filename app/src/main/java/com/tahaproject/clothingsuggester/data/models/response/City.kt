package com.tahaproject.clothingsuggester.data.models.response

import com.google.gson.annotations.SerializedName

data class City(
    val name: String,
    @SerializedName("coord") val coordinates: Coordinates,
    val country: String
)