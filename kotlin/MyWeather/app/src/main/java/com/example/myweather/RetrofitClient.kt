package com.example.myweather

import android.content.Context
import android.content.Intent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private const val API_KEY = "3cca6949aed6929337a048907a050252"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherService: WeatherService by lazy { retrofit.create(WeatherService::class.java) }

    fun getWeatherByCityName(context: Context, cityName: String) {
        val call = weatherService.getWeatherByCityName(cityName, API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    handleWeatherData(context, weatherData)
                } else {
                    handleWeatherFailure(response.message())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                handleWeatherFailure(t.message!!)
            }
        })
    }

    private fun handleWeatherData(context: Context, weatherData: WeatherResponse?) {
        if (weatherData != null) {
            val intent = Intent("com.example.MyWeather.ACTION_WEATHER_DATA")
            intent.putExtra("cityName", weatherData.name)
            intent.putExtra("temperature", weatherData.main?.temp)
            intent.putExtra("maxTemperature", weatherData.main?.temp_max)
            intent.putExtra("minTemperature", weatherData.main?.temp_min)
            val weatherStringArray = arrayListOf<String>()
            for(weather in weatherData.weather) {
                weatherStringArray += "main:${weather.main},description:${weather.description}"
            }
            intent.putStringArrayListExtra("weather", weatherStringArray)
            context.sendBroadcast(intent)

            printWeatherData(weatherData)
        }
    }

    private fun printWeatherData(weatherData: WeatherResponse?) {
        if (weatherData != null) {
            println("cord: lat:${weatherData.coord?.lat},lon:${weatherData.coord?.lon}")
            for (weather in weatherData.weather) {
                println(
                    "weather: id:${weather.id},main:${weather.main}," +
                            "description:${weather.description},icon:${weather.icon}"
                )
            }
            println("base:${weatherData.base}")
            println(
                "main: temperature:${weatherData.main?.temp},pressure:${weatherData.main?.pressure}," +
                        "humidity:${weatherData.main?.humidity},temperature_min:${weatherData.main?.temp_min}," +
                        "temperature_max:${weatherData.main?.temp_max}"
            )
            println("visibility:${weatherData.visibility}")
            println("wind: speed:${weatherData.wind?.speed},deq:${weatherData.wind?.deg}")
            println("clouds: clouds:${weatherData.clouds?.clouds}")
            println("dt: ${weatherData.dt}")
            println(
                "sys: type:${weatherData.sys?.type},id:${weatherData.sys?.id},message:${weatherData.sys?.message}" +
                        ",country:${weatherData.sys?.country},+sunrise:${weatherData.sys?.sunrise},+sunset:${weatherData.sys?.sunset}"
            )
            println("id: ${weatherData.id}")
            println("name: ${weatherData.name}")
            println("cod: ${weatherData.cod}")
        }
    }

    private fun handleWeatherFailure(message: String) {
        println("Request failed: $message")
    }
}