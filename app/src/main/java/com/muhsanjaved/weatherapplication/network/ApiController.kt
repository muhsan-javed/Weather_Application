package com.muhsanjaved.weatherapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class ApiController {
    private val BASE_URI :String = "https://api.openweathermap.org/data/2.5/"
    private var retrofit : Retrofit? = null

    fun getApiInterface() : ApiInterface? {
        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiInterface::class.java)
    }
}