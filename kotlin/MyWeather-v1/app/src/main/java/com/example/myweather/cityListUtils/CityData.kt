package com.example.myweather.cityListUtils


data class Coord (
    val lon: Double,
    val lat: Double
)
data class CityData(
    val id: Int,
    val name: String,
    val state: String?=null,
    val country: String,
    val coord: Coord
)