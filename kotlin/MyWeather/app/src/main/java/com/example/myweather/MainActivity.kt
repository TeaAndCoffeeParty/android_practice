package com.example.myweather

import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var weatherResponseReceiver: WeatherResponseReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherResponseReceiver = WeatherResponseReceiver()
        weatherResponseReceiver.setTextView(findViewById<TextView>(R.id.weatherResult))
        val intentFilter = IntentFilter("com.example.MyWeather.ACTION_WEATHER_DATA")
        registerReceiver(weatherResponseReceiver, intentFilter)

        findViewById<Button>(R.id.buttonSearch).setOnClickListener { searchCityNameWeather(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(weatherResponseReceiver)
    }

    private fun searchCityNameWeather(view: View) {
        val cityName = findViewById<EditText>(R.id.editTextCity).text.toString().trim()
        RetrofitClient.getWeatherByCityName(view.context, cityName)
    }



}