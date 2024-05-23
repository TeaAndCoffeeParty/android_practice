package com.example.myweather

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.cityListUtils.CityData
import com.example.myweather.cityListUtils.CityDataAdapter
import com.example.myweather.cityListUtils.CityListDataManager
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.event.CityDataListReadyEvent
import com.example.myweather.event.ForecastResponseEvent
import com.example.myweather.locationUtils.LocationManagerUtils
import com.example.myweather.openWeatherMap.ForecastAdapter
import com.example.myweather.openWeatherMap.ForecastResponse
import com.example.myweather.ui.CityWeatherFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), LocationManagerUtils.PermissionCallback {
    private val tag = "MainActivity"
    private lateinit var binding : ActivityMainBinding
    private lateinit var cityListManager : CityListDataManager
    private lateinit var locationManagerUtils: LocationManagerUtils
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

        locationManagerUtils = LocationManagerUtils(this).apply {
            setPermissionCallback(this@MainActivity)
            requestLocationPermission(this@MainActivity)
        }
        EventBus.getDefault().register(this)
        initView()

        cityListManager = CityListDataManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManagerUtils.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CityWeatherFragment())
            .commit()

        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cityDataRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cityDataRecyclerView.adapter = CityDataAdapter(emptyList<CityData>())
        binding.searchBar.apply {
            setOnClickListener { binding.searchView.show() }
        }
        binding.searchView.editText.setOnEditorActionListener { v, _, _ ->
            val filterText = v.editableText.toString()
            Toast.makeText(v.context, "the text: $filterText", Toast.LENGTH_LONG).show()
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

    private fun updateForecastList(forecastResponse: ForecastResponse) {
        val adapter = ForecastAdapter(forecastResponse.forecastCellList!!)
        binding.forecastRecyclerView.adapter = adapter
    }

    private fun updateCityDataList(cityDataList: List<CityData>) {
        val adapter = CityDataAdapter(cityDataList)
        adapter.onItemClick = { cityData ->
            val cityName = cityData.name
            RetrofitClient.getWeatherByCityName(cityName)
            RetrofitClient.getForecastByCityName(cityName)
            val message = "Click item name: $cityName"
            binding.searchView.hide()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        binding.cityDataRecyclerView.adapter = adapter
    }

    override fun onPermissionGranted() {
        Log.d(tag, "onPermissionGranted")
        locationManagerUtils.requestGPSLocationUpdates()
    }

    override fun onPermissionDenied() {
        Toast.makeText(this, R.string.requiredPermissionPrompt, Toast.LENGTH_SHORT).show()
        ActivityCompat.finishAffinity(this)
    }

    override fun onLocationChanged(location: Location) {
        Log.d(tag, "latitude:${location.latitude}, longitude:${location.longitude}")
    }
}