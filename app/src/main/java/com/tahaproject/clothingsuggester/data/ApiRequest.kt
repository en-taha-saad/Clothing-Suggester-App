package com.tahaproject.clothingsuggester.data

import com.google.gson.Gson
import com.tahaproject.clothingsuggester.util.Constants
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

open class ApiRequest {
    val gson = Gson()
    val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun makeRequest(endPoint: String): Request.Builder {
        return Request.Builder()
            .url("${Constants.URL}/$endPoint")
    }

    open fun postRequest(body: FormBody, endPoint: String): Request =
        makeRequest(endPoint).post(
            body
        ).build()


    open fun getRequest(endPoint: String): Request = makeRequest(endPoint).get().build()

    open fun putRequest(body: FormBody, endPoint: String): Request =
        makeRequest(endPoint).put(
            body
        ).build()
}