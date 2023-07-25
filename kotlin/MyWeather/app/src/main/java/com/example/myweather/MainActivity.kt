package com.example.myweather

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonSearch).setOnClickListener { searchCityNameWeather(it) }
    }

    private fun searchCityNameWeather(view: View) {
        var cityName = findViewById<EditText>(R.id.editTextCity).text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
    }
}