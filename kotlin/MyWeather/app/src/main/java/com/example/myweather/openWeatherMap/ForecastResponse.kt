package com.example.myweather.openWeatherMap

import com.example.myweather.WeatherResponseClouds
import com.example.myweather.WeatherResponseCoord
import com.example.myweather.WeatherResponseWeather
import com.google.gson.annotations.SerializedName

data class ForecastResponse (
    @SerializedName("cod")
    var cod: String = "",
    @SerializedName("message")
    var message: Int = 0,
    @SerializedName("cnt")
    var cnt : Int = 0,
    @SerializedName("list")
    var forecastCellList : ArrayList<ForecastCell>? = null,
    @SerializedName("city")
    var forecastCity: ForecastCity? = null
)


data class ForecastCell (
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("main")
    val main: ForecastMain,
    @SerializedName("weather")
    val weather: List<WeatherResponseWeather>,
    @SerializedName("clouds")
    val clouds: WeatherResponseClouds,
    @SerializedName("wind")
    val wind: ForecastWind,
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("pop")
    val pop: Double = 0.0,
    @SerializedName("rain")
    val rain: ForecastRain,
    @SerializedName("snow")
    val snow: ForecastSnow,
    @SerializedName("sys")
    val sys: ForecastSys,
    @SerializedName("dt_txt")
    val dt_txt: String = ""
)

data class ForecastCity(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("coord")
    val coord: WeatherResponseCoord,
    @SerializedName("country")
    val country: String ="",
    @SerializedName("population")
    val population:Int = 0,
    @SerializedName("timezone")
    val timezone: Int = 0,
    @SerializedName("sunrise")
    val sunrise: Int = 0,
    @SerializedName("sunset")
    val sunset: Int = 0
)

data class ForecastMain(
    @SerializedName("temp")
    val temperature: Double = 0.0,
    @SerializedName("feels_like")
    val feelsLike: Double = 0.0,
    @SerializedName("temp_min")
    val minTemperature: Double = 0.0,
    @SerializedName("temp_max")
    val maxTemperature: Double = 0.0,
    @SerializedName("pressure")
    val pressure: Int = 0,
    @SerializedName("sea_level")
    val seaLevel: Int = 0,
    @SerializedName("grnd_level")
    val groundLevel: Int = 0,
    @SerializedName("humidity")
    val humidity: Int = 0,
    @SerializedName("temp_kf")
    val temperatureKf: Double = 0.0
)

data class ForecastWind(
    @SerializedName("speed")
    val speed: Double = 0.0,
    @SerializedName("deg")
    val degree: Int = 0,
    @SerializedName("gust")
    val gust : Double = 0.0
)

data class ForecastRain(
    @SerializedName("3h")
    val heightInThreeHours: Double = 0.0
)

data class ForecastSnow(
    @SerializedName("3h")
    val heightInThreeHours: Double = 0.0
)

data class ForecastSys(
    @SerializedName("pod")
    val partOfDay: String = ""
)
