package com.example.myweather

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    var coord: Coord? = null,
    @SerializedName("weather")
    var weather : ArrayList<Weather>,
    @SerializedName("base")
    var base: String? = null,
    @SerializedName("main")
    var main: Main? = null,
    @SerializedName("visibility")
    var visibility: Int = 0,
    @SerializedName("wind")
    var wind: Wind ?= null,
    @SerializedName("clouds")
    var clouds: Clouds ? = null,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("sys")
    var sys: Sys ? = null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String ?= null,
    @SerializedName("cod")
    var cod: Int
)

class Coord {
    @SerializedName("lon")
    var lon : Float = 0.toFloat()
    @SerializedName("lat")
    var lat : Float = 0.toFloat()
}

class Weather {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("main")
    var main: String ?= null
    @SerializedName("description")
    var description: String ?= null
    @SerializedName("icon")
    var icon: String ?= null
}

class Main {
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

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Int = 0
}

class Clouds {
    @SerializedName("clouds")
    var clouds: Int = 0
}

class Sys {
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