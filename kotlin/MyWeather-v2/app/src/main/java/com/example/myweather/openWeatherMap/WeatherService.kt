package com.example.myweather.openWeatherMap

import com.example.myweather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    /* https://openweathermap.org/current */
    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") cityName : String,
        @Query("appid") apiKey : String
    ) : Call<WeatherResponse>
    @GET("weather")
    fun getWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apkKey: String
    ) : Call<WeatherResponse>

    /* https://openweathermap.org/forecast5 */
    @GET("forecast")
    fun getForecastByCityName(
        @Query("q") cityName : String,
        @Query("appid") apiKey : String
    ) : Call<ForecastResponse>

    @GET("forecast")
    fun getForecastByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apkKey: String
    ) : Call<ForecastResponse>

}