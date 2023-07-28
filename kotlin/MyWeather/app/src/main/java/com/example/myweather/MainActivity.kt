package com.example.myweather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.myweather.event.ForecastResponseEvent
import com.example.myweather.event.WeatherResponseEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        findViewById<Button>(R.id.buttonSearch).setOnClickListener { searchCityNameWeather() }

        val testCityName = "Shaoxing"
        RetrofitClient.getWeatherByCityName(testCityName)
        RetrofitClient.getForecastByCityName(testCityName)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveWeatherResponse(event: WeatherResponseEvent) {
        val weatherResponse = event.weatherResponse
        val kelvins = 273.15
        val cityName = weatherResponse.name
        val temperature = weatherResponse.weatherResponseMain?.temp?.minus(kelvins)
        val maxTemperature = weatherResponse.weatherResponseMain?.temp_max?.minus(kelvins)
        val minTemperature = weatherResponse.weatherResponseMain?.temp_min?.minus(kelvins)

        findViewById<TextView>(R.id.textViewCityName).text = cityName
        findViewById<TextView>(R.id.textViewTemperature).text = temperature?.toInt().toString()
        findViewById<TextView>(R.id.textViewMaxMinTemperature).text = "${maxTemperature?.toInt()} / ${minTemperature?.toInt()}"
        findViewById<TextView>(R.id.textViewWeather).text = "${weatherResponse.weatherResponseWeather.first().main} | ${weatherResponse.weatherResponseWeather.first().description}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveForecastResponse(event: ForecastResponseEvent) {
        val forecastResponse = event.forecastResponse
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        for (cell in forecastResponse.forecastCellList!!) {
//            println("dt:${cell.dt},date:${formattedDateTime}")
            println(simpleDateFormat.format(cell.dt*1000L))
        }

    }

    private fun searchCityNameWeather() {
        val cityName = findViewById<EditText>(R.id.editTextCity).text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
        RetrofitClient.getForecastByCityName(cityName)
    }
}