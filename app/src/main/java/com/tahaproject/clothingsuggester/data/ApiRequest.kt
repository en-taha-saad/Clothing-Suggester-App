package com.tahaproject.clothingsuggester.data

import com.google.gson.Gson
import com.tahaproject.clothingsuggester.data.models.requests.Location
import com.tahaproject.clothingsuggester.util.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class ApiRequest {
    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build()

    private fun getUrlBuilder(location: Location, appid: String): HttpUrl {
        return HttpUrl.Builder()
            .scheme(Constants.API_SCHEME)
            .host(Constants.API_HOST)
            .addPathSegment(Constants.API_DATA_SEGMENT)
            .addPathSegment(Constants.API_VERSION_SEGMENT)
            .addPathSegment(Constants.API_FORECAST_SEGMENT)
            .addQueryParameter(Constants.API_LAT, location.lat)
            .addQueryParameter(Constants.API_LON, location.lon)
            .addQueryParameter(Constants.API_APPID, appid)
            .addQueryParameter(Constants.API_UNITS, Constants.API_DEFAULT_UNITS)
            .build()
    }

    fun getCurrentWeather(location: Location, appid: String, callback: Callback) {
        val request = Request.Builder()
            .url(getUrlBuilder(location, appid)).build()
        client.newCall(request).enqueue(callback)
    }

}