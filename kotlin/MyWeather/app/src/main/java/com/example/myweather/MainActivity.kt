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

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WeatherResponseEvent) {
        val weatherResponse = event.weatherResponse
        val kelvins = 273.15
        val cityName = weatherResponse.name
        val temperature = weatherResponse.main?.temp?.minus(kelvins)
        val maxTemperature = weatherResponse.main?.temp_max?.minus(kelvins)
        val minTemperature = weatherResponse.main?.temp_min?.minus(kelvins)

        findViewById<TextView>(R.id.textViewCityName).text = cityName
        findViewById<TextView>(R.id.textViewTemperature).text = temperature?.toInt().toString()
        findViewById<TextView>(R.id.textViewMaxMinTemperature).text = "${maxTemperature?.toInt()} / ${minTemperature?.toInt()}"
        findViewById<TextView>(R.id.textViewWeather).text = "${weatherResponse.weather.first().main} | ${weatherResponse.weather.first().description}"

    }

    private fun searchCityNameWeather() {
        val cityName = findViewById<EditText>(R.id.editTextCity).text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
    }



}