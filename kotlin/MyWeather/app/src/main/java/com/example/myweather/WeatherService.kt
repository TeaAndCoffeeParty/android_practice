package com.example.myweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") cityName : String,
        @Query("appid") apiKey : String
    ) : Call<WeatherResponse>

//    @GET("forecast")
//    fun getForecastByCityName(@Query("q") cityName : String, @Query("appid") apiKey : String) : Call<ForecastResponse>
}