package com.example.myweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.myweather.event.WeatherResponseEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        findViewById<Button>(R.id.buttonSearch).setOnClickListener { searchCityNameWeather() }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WeatherResponseEvent) {
        val weatherResponse = event.weatherResponse
        val kelvins = 273.15
        val cityName = weatherResponse.name
        val temperature = weatherResponse.main?.temp
        val maxTemperature = weatherResponse.main?.temp_max?.minus(kelvins)
        val minTemperature = weatherResponse.main?.temp_min?.minus(kelvins)
        val decimalFormat = DecimalFormat("#.#")
        val weatherStringArray = arrayListOf<String>()
        for(weather in weatherResponse.weather) {
            weatherStringArray += "main:${weather.main},description:${weather.description}"
        }

        @SuppressLint("SetTextI18n")
        findViewById<TextView>(R.id.weatherResult).text = "cityName:$cityName\n" +
                    "temperature:${decimalFormat.format(temperature)}\n" +
                "maxTemperature:${decimalFormat.format(maxTemperature)}\n" +
                "minTemperature:${decimalFormat.format(minTemperature)}\n" +
                "weather:$weatherStringArray"
    }

    private fun searchCityNameWeather() {
        val cityName = findViewById<EditText>(R.id.editTextCity).text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
    }



}