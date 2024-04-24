package com.example.myweather.event

import com.example.myweather.WeatherResponse
import com.example.myweather.cityListUtils.CityData
import com.example.myweather.openWeatherMap.ForecastResponse

class WeatherResponseEvent(val weatherResponse: WeatherResponse)
class ForecastResponseEvent(val forecastResponse: ForecastResponse)

class CityDataListReadyEvent(val cityDataList: List<CityData>)