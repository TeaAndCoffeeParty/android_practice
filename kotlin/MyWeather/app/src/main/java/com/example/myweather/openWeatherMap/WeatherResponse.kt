package com.example.myweather

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    var weatherResponseCoord: WeatherResponseCoord? = null,
    @SerializedName("weather")
    var weatherResponseWeather : ArrayList<WeatherResponseWeather>,
    @SerializedName("base")
    var base: String? = null,
    @SerializedName("main")
    var weatherResponseMain: WeatherResponseMain? = null,
    @SerializedName("visibility")
    var visibility: Int = 0,
    @SerializedName("wind")
    var weatherResponseWind: WeatherResponseWind ?= null,
    @SerializedName("clouds")
    var clouds: WeatherResponseClouds ? = null,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("sys")
    var weatherResponseSys: WeatherResponseSys ? = null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String ?= null,
    @SerializedName("cod")
    var cod: Int
)

class WeatherResponseCoord {
    @SerializedName("lon")
    var lon : Float = 0.toFloat()
    @SerializedName("lat")
    var lat : Float = 0.toFloat()
}

class WeatherResponseWeather {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("main")
    var main: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("icon")
    var icon: String = ""
}

class WeatherResponseMain {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Int = 0
    @SerializedName("humidity")
    var humidity: Int = 0
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()
}

class WeatherResponseWind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Int = 0
}

class WeatherResponseClouds {
    @SerializedName("clouds")
    var clouds: Int = 0
}

class WeatherResponseSys {
    @SerializedName("type")
    var type: Int = 0
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("message")
    var message: Float = 0.toFloat()
    @SerializedName("country")
    var country: String ?= null
    @SerializedName("sunrise")
    var sunrise: Int = 0
    @SerializedName("sunset")
    var sunset: Int = 0
}