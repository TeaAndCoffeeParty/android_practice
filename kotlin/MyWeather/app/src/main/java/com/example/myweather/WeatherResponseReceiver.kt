package com.example.myweather

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView
import java.text.DecimalFormat

class WeatherResponseReceiver : BroadcastReceiver() {
    private var textView: TextView? = null

    fun setTextView(textView: TextView) {
        this.textView = textView
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "com.example.MyWeather.ACTION_WEATHER_DATA") {
            val kelvins = 273.15
            val cityName = intent.getStringExtra("cityName")
            val temperature = intent.getFloatExtra("temperature", 0.0F) - kelvins
            val maxTemperature = intent.getFloatExtra("maxTemperature", 0.0F) - kelvins
            val minTemperature = intent.getFloatExtra("minTemperature", 0.0F) - kelvins
            val weather = intent.getStringArrayListExtra("weather")
            val decimalFormat = DecimalFormat("#.#")

            @SuppressLint("SetTextI18n")
            textView?.text = "$cityName\n${decimalFormat.format(temperature)}\n${decimalFormat.format(maxTemperature)}\n${decimalFormat.format(minTemperature)}\n$weather"
        }
    }
}

