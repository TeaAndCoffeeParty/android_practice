package com.example.myweather

import android.util.Log
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
    private const val TAG = "RetrofitClient"
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
        Log.d(TAG,"handleForecastFailure:${message}")
    }

    private fun handleForecastData(forecastData: ForecastResponse?) {
        if(forecastData == null) return

        val forecastResponseEvent = ForecastResponseEvent(forecastData)
        EventBus.getDefault().post(forecastResponseEvent)

        printForecastResponse(forecastData)
    }

    private  fun printForecastResponse(forecastResponse: ForecastResponse) {
        Log.d(TAG,"cod:${forecastResponse.cod}")
        Log.d(TAG,"message:${forecastResponse.message}")
        Log.d(TAG,"cnt:${forecastResponse.cnt}")
        Log.d(TAG,"list:${forecastResponse.forecastCellList?.size}")
        Log.d(TAG,"city id:${forecastResponse.forecastCity?.id} name:${forecastResponse.forecastCity?.name}")
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
            Log.d(TAG,"cord: lat:${weatherData.weatherResponseCoord?.lat},lon:${weatherData.weatherResponseCoord?.lon}")
            for (weather in weatherData.weatherResponseWeather) {
                Log.d(TAG,
                    "weather: id:${weather.id},main:${weather.main}," +
                            "description:${weather.description},icon:${weather.icon}"
                )
            }
            Log.d(TAG,"base:${weatherData.base}")
            Log.d(TAG,
                "main: temperature:${weatherData.weatherResponseMain?.temp},pressure:${weatherData.weatherResponseMain?.pressure}," +
                        "humidity:${weatherData.weatherResponseMain?.humidity},temperature_min:${weatherData.weatherResponseMain?.temp_min}," +
                        "temperature_max:${weatherData.weatherResponseMain?.temp_max}"
            )
            Log.d(TAG,"visibility:${weatherData.visibility}")
            Log.d(TAG,"wind: speed:${weatherData.weatherResponseWind?.speed},deq:${weatherData.weatherResponseWind?.deg}")
            Log.d(TAG,"clouds: clouds:${weatherData.clouds?.clouds}")
            Log.d(TAG,"dt: ${weatherData.dt}")
            Log.d(TAG,
                "sys: type:${weatherData.weatherResponseSys?.type},id:${weatherData.weatherResponseSys?.id},message:${weatherData.weatherResponseSys?.message}" +
                        ",country:${weatherData.weatherResponseSys?.country},+sunrise:${weatherData.weatherResponseSys?.sunrise},+sunset:${weatherData.weatherResponseSys?.sunset}"
            )
            Log.d(TAG,"id: ${weatherData.id}")
            Log.d(TAG,"name: ${weatherData.name}")
            Log.d(TAG,"cod: ${weatherData.cod}")
        }
    }

    private fun handleWeatherFailure(message: String) {
        Log.d(TAG,"Request failed: $message")
    }
}