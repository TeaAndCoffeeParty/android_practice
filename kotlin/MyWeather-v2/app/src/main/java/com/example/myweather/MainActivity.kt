package com.example.myweather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.cityListUtils.CityListDataManager
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.event.ForecastResponseEvent
import com.example.myweather.event.WeatherResponseEvent
import com.example.myweather.openWeatherMap.ForecastAdapter
import com.example.myweather.openWeatherMap.ForecastResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private val kelvins = 273.15
    private lateinit var binding : ActivityMainBinding
    private lateinit var cityListManager : CityListDataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        EventBus.getDefault().register(this)
        binding.searchBarLayout.buttonSearch.setOnClickListener { searchCityNameWeather() }

        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        cityListManager = CityListDataManager(this)
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

        binding.cityWeatherLayout.textViewCityName.text = cityName
        binding.cityWeatherLayout.textViewTemperature.text = temperature?.toInt().toString()
        binding.cityWeatherLayout.textViewMaxMinTemperature.text =
            "${maxTemperature?.toInt()} / ${minTemperature?.toInt()}"
        binding.cityWeatherLayout.textViewWeather.text =
            weatherResponse.weatherResponseWeather.first().main + " | " +
                    weatherResponse.weatherResponseWeather.first().description
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveForecastResponse(event: ForecastResponseEvent) {
        updateForecastList(event.forecastResponse)
    }

    private fun searchCityNameWeather() {
        val cityName = binding.searchBarLayout.editTextCity.text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
        RetrofitClient.getForecastByCityName(cityName)
    }

    private fun updateForecastList(forecastResponse: ForecastResponse) {

        val adapter = ForecastAdapter(forecastResponse.forecastCellList!!)
        binding.forecastRecyclerView.adapter = adapter
    }
}