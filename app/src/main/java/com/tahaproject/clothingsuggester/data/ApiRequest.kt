package com.tahaproject.clothingsuggester.data

import com.google.gson.Gson
import com.tahaproject.clothingsuggester.util.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

open class ApiRequest {
    val gson = Gson()
    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build()

    private fun getUrlBuilder(lat: String, lon: String, appid: String): HttpUrl {
        return HttpUrl.Builder()
            .scheme(Constants.API_SCHEME)
            .host(Constants.API_HOST)
            .addPathSegment(Constants.API_DATA_SEGMENT)
            .addPathSegment(Constants.API_VERSION_SEGMENT)
            .addPathSegment(Constants.API_FORECAST_SEGMENT)
            .addQueryParameter("lat", lat)
            .addQueryParameter("lon", lon)
            .addQueryParameter("appid", appid)
            .addQueryParameter("units", Constants.API_DEFAULT_UNITS)
            .build()
    }

    private fun getCurrentWeather(lat: String, lon: String, appid: String, callback: Callback) {
        val request = Request.Builder()
            .url(getUrlBuilder(lat, lon, appid)).build()
        client.newCall(request).enqueue(callback)
    }

}