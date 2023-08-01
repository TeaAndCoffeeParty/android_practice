package com.example.myweather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.myweather.event.ForecastResponseEvent
import com.example.myweather.event.WeatherResponseEvent
import com.example.myweather.openWeatherMap.ForecastResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val kelvins = 273.15

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
        updateForecastList(event.forecastResponse)
    }

    private fun searchCityNameWeather() {
        val cityName = findViewById<EditText>(R.id.editTextCity).text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
        RetrofitClient.getForecastByCityName(cityName)
    }

    private fun updateForecastList(forecastResponse: ForecastResponse) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        val data = mutableListOf<String>()
        for (cell in forecastResponse.forecastCellList!!) {
            val oneLine = "${simpleDateFormat.format(cell.dt*1000L)}\n" +
                    "temperature:${cell.main.temperature.minus(kelvins).toInt()}," +
                    "feel_like:${cell.main.feelsLike.minus(kelvins).toInt()},\n" +
                    "weather:${cell.weather.first().main},${cell.weather.first().description}"
            data.add(oneLine)
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        findViewById<ListView>(R.id.listViewTodayForecast).adapter = adapter
    }
}