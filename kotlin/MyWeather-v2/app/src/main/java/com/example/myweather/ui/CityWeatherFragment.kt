package com.example.myweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myweather.databinding.FragmentCityWeatherBinding
import com.example.myweather.event.WeatherResponseEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CityWeatherFragment : Fragment() {
    private val tag = "CityWeatherFragment"
    private val kelvins = 273.15
    private lateinit var binding : FragmentCityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        Log.d(tag, "onCreateView running")
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveWeatherResponse(event: WeatherResponseEvent) {
        Log.d(tag, "onReceiveWeatherResponse running")
        val weatherResponse = event.weatherResponse
        val cityName = weatherResponse.name
        val temperature = weatherResponse.weatherResponseMain?.temp?.minus(kelvins)
        val maxTemperature = weatherResponse.weatherResponseMain?.temp_max?.minus(kelvins)
        val minTemperature = weatherResponse.weatherResponseMain?.temp_min?.minus(kelvins)

        binding.textViewCityName.text = cityName
        binding.textViewTemperature.text = temperature?.toInt().toString()
        binding.textViewMaxMinTemperature.text =
            "${maxTemperature?.toInt()} / ${minTemperature?.toInt()}"
        binding.textViewWeather.text =
            weatherResponse.weatherResponseWeather.first().main + " | " +
                    weatherResponse.weatherResponseWeather.first().description
    }

}