package com.example.myweather.cityListUtils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.RetrofitClient


class CityDataAdapter(private val context: Context, private val originCityDataList: List<CityData>) :
    RecyclerView.Adapter<CityDataAdapter.ViewHolder>() {

    private var filterCityDataList : MutableList<CityData> = originCityDataList.toMutableList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }
        val cityDataId : TextView = view.findViewById<TextView>(R.id.city_data_id)
        val cityDataName : TextView = view.findViewById<TextView>(R.id.city_data_name)
        val cityDataCountry : TextView = view.findViewById<TextView>(R.id.city_data_country)
        val cityDataCoordinate: TextView = view.findViewById<TextView>(R.id.city_data_coordinate)
        override fun onClick(p0: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                val cityName = filterCityDataList[position].name
                RetrofitClient.getWeatherByCityName(cityName)
                RetrofitClient.getForecastByCityName(cityName)
                val message = "Click item name: ${filterCityDataList[position].name}"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.city_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityData = filterCityDataList[position]
        holder.cityDataId.text = cityData.id.toString()
        holder.cityDataName.text = cityData.name
        holder.cityDataCountry.text = cityData.country
        holder.cityDataCoordinate.text = buildString {
            append(String.format("%.1f", cityData.coord.lon))
            append(",")
            append(String.format("%.1f", cityData.coord.lat))
        }
    }
    override fun getItemCount() = filterCityDataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setFilter(filterText: String) {
        if(filterText.isEmpty()) {
            filterCityDataList.clear()
            filterCityDataList.addAll(originCityDataList)
        } else {
            filterCityDataList.clear()
            for (item in originCityDataList) {
                if (item.name.lowercase().contains(filterText.lowercase())) {
                    filterCityDataList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}