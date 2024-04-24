package com.example.myweather.openWeatherMap

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private val forecastList: List<ForecastCell>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val weatherData: TextView = view.findViewById(R.id.weatherData)
            val weatherDescription: TextView = view.findViewById(R.id.weatherDescription)
            val weatherTemperature: TextView = view.findViewById(R.id.weatherTemperature)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kelvins = 273.15
        val simpleDateFormat = SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH)
        val forecastCell = forecastList[position]
        holder.weatherData.text = simpleDateFormat.format(forecastCell.dt*1000L)
        holder.weatherDescription.text = forecastCell.weather.first().main
        holder.weatherTemperature.text = "${forecastCell.main.maxTemperature.minus(kelvins).toInt()}/" +
                "${forecastCell.main.minTemperature.minus(kelvins).toInt()}"
    }

    override fun getItemCount() = forecastList.size
}