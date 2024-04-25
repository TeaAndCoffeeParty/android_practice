package com.example.myweather.cityListUtils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R


class CityDataAdapter(private val originCityDataList: List<CityData>) :
    RecyclerView.Adapter<CityDataAdapter.ViewHolder>() {

    private var filterCityDataList : MutableList<CityData> = originCityDataList.toMutableList()
    var onItemClick: ((CityData) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if(absoluteAdapterPosition != RecyclerView.NO_POSITION)
                    onItemClick?.invoke(filterCityDataList[absoluteAdapterPosition])
            }
        }
        val cityDataId : TextView = view.findViewById<TextView>(R.id.city_data_id)
        val cityDataName : TextView = view.findViewById<TextView>(R.id.city_data_name)
        val cityDataCountry : TextView = view.findViewById<TextView>(R.id.city_data_country)
        val cityDataCoordinate: TextView = view.findViewById<TextView>(R.id.city_data_coordinate)
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