package com.example.myweather

import com.example.myweather.event.ForecastResponseEvent
import com.example.myweather.event.WeatherResponseEvent
import com.example.myweather.openWeatherMap.ForecastResponse
import com.example.myweather.openWeatherMap.WeatherService
import org.greenrobot.eventbus.EventBus
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

    fun getWeatherByCityName(cityName: String) {
        val call = weatherService.getWeatherByCityName(cityName, API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    handleWeatherData(weatherData)
                } else {
                    handleWeatherFailure(response.message())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                handleWeatherFailure(t.message!!)
            }
        })
    }

    fun getForecastByCityName(cityName: String) {
        val call = weatherService.getForecastByCityName(cityName, API_KEY)
        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call : Call<ForecastResponse>,
                response: Response<ForecastResponse>) {
                if(response.isSuccessful) {
                    val forecastData = response.body()
                    handleForecastData(forecastData)
                } else {
                    handleForecastFailure(response.message())
                }

            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                handleForecastFailure(t.message!!)
            }
        })
    }

    private fun handleForecastFailure(message: String) {
        println("handleForecastFailure:${message}")
    }

    private fun handleForecastData(forecastData: ForecastResponse?) {
        if(forecastData == null) return

        val forecastResponseEvent = ForecastResponseEvent(forecastData)
        EventBus.getDefault().post(forecastResponseEvent)

        printForecastResponse(forecastData)
    }

    private  fun printForecastResponse(forecastResponse: ForecastResponse) {
        println("cod:${forecastResponse.cod}")
        println("message:${forecastResponse.message}")
        println("cnt:${forecastResponse.cnt}")
        println("list:${forecastResponse.forecastCellList?.size}")
        println("city id:${forecastResponse.forecastCity?.id} name:${forecastResponse.forecastCity?.name}")
    }

    private fun handleWeatherData(weatherData: WeatherResponse?) {
        if (weatherData != null) {
            val weatherResponseEvent = WeatherResponseEvent(weatherData)
            EventBus.getDefault().post(weatherResponseEvent)

            printWeatherData(weatherData)
        }
    }

    private fun printWeatherData(weatherData: WeatherResponse?) {
        if (weatherData != null) {
            println("cord: lat:${weatherData.weatherResponseCoord?.lat},lon:${weatherData.weatherResponseCoord?.lon}")
            for (weather in weatherData.weatherResponseWeather) {
                println(
                    "weather: id:${weather.id},main:${weather.main}," +
                            "description:${weather.description},icon:${weather.icon}"
                )
            }
            println("base:${weatherData.base}")
            println(
                "main: temperature:${weatherData.weatherResponseMain?.temp},pressure:${weatherData.weatherResponseMain?.pressure}," +
                        "humidity:${weatherData.weatherResponseMain?.humidity},temperature_min:${weatherData.weatherResponseMain?.temp_min}," +
                        "temperature_max:${weatherData.weatherResponseMain?.temp_max}"
            )
            println("visibility:${weatherData.visibility}")
            println("wind: speed:${weatherData.weatherResponseWind?.speed},deq:${weatherData.weatherResponseWind?.deg}")
            println("clouds: clouds:${weatherData.clouds?.clouds}")
            println("dt: ${weatherData.dt}")
            println(
                "sys: type:${weatherData.weatherResponseSys?.type},id:${weatherData.weatherResponseSys?.id},message:${weatherData.weatherResponseSys?.message}" +
                        ",country:${weatherData.weatherResponseSys?.country},+sunrise:${weatherData.weatherResponseSys?.sunrise},+sunset:${weatherData.weatherResponseSys?.sunset}"
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