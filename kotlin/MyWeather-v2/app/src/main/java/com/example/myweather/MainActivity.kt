package com.example.myweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.cityListUtils.CityData
import com.example.myweather.cityListUtils.CityDataAdapter
import com.example.myweather.cityListUtils.CityListDataManager
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.event.CityDataListReadyEvent
import com.example.myweather.event.ForecastResponseEvent
import com.example.myweather.event.WeatherResponseEvent
import com.example.myweather.openWeatherMap.ForecastAdapter
import com.example.myweather.openWeatherMap.ForecastResponse
import com.example.myweather.ui.CityWeatherFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private val kelvins = 273.15
    private lateinit var binding : ActivityMainBinding
    private lateinit var cityListManager : CityListDataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        EventBus.getDefault().register(this)
        initView()

        cityListManager = CityListDataManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CityWeatherFragment())
            .commit()

        binding.searchBarLayout.buttonSearch.setOnClickListener { searchCityNameWeather() }

        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cityDataRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cityDataRecyclerView.adapter = CityDataAdapter(this, emptyList<CityData>())
        binding.searchBar.apply {
            setOnClickListener { binding.searchView.show() }
        }
        binding.searchView.editText.setOnEditorActionListener { v, _, _ ->
            val filterText = v.editableText.toString()
            Toast.makeText(v.context, "the text: $filterText", Toast.LENGTH_SHORT).show()
            val cityDataAdapter : CityDataAdapter= binding.cityDataRecyclerView.adapter as CityDataAdapter
            cityDataAdapter.setFilter(filterText)
            return@setOnEditorActionListener false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveForecastResponse(event: ForecastResponseEvent) {
        updateForecastList(event.forecastResponse)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveCityDataListReadyEvent(event: CityDataListReadyEvent) {
        Log.d(tag, "on received city data list ready event ${event.cityDataList.size}")
        updateCityDataList(event.cityDataList)
    }

    private fun searchCityNameWeather() {
        val cityName = binding.searchBarLayout.editTextCity.text.toString().trim()
        RetrofitClient.getWeatherByCityName(cityName)
        RetrofitClient.getForecastByCityName(cityName)
    }

    private fun updateForecastList(forecastResponse: ForecastResponse) {
        val adapter = ForecastAdapter(forecastResponse.forecastCellList!!)
        binding.forecastRecyclerView.adapter = adapter
    }

    private fun updateCityDataList(cityDataList: List<CityData>) {
        val adapter = CityDataAdapter(this, cityDataList)
        binding.cityDataRecyclerView.adapter = adapter
    }
}