package com.muhsanjaved.weatherapplication.network

import com.muhsanjaved.weatherapplication.models.WeatherApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

   /* var weather: String
        get() = "weather"
        set(value) = TODO()*/


    @GET("weather")
    fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<WeatherApp>
}