package com.example.myweather.cityListUtils

import android.content.Context
import android.util.Log
import com.example.myweather.event.CityDataListReadyEvent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream


class CityListDataManager(private val context: Context) {
    private val tag = "CityListDataManager"
    private val jsonFileName = "city.list.json"
    private val cityListJsonFile : File = File(context.filesDir, jsonFileName)
    private lateinit var cityDataList : Array<CityData>
    init {
        CoroutineScope(Dispatchers.IO).launch {
            if(!isExistCityListJsonFile()) {
                unzipGzFile(context)
            }
            readContentOneByOne(context)
        }
    }

    private fun isExistCityListJsonFile() : Boolean {
        val isExisted = cityListJsonFile.exists()
        Log.d(tag, "city list json file is existed:$isExisted")
        return isExisted
    }

    private suspend fun unzipGzFile(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                context.resources.openRawResource(
                    com.example.myweather.R.raw.city_list_json).use { rawIn ->
                    GZIPInputStream(rawIn).use { gzipIn ->
                        FileOutputStream(cityListJsonFile).use { fileOut ->
                            gzipIn.copyTo(fileOut)
                        }
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun readContentOneByOne(context: Context) = try {
        val gson = Gson()
        val file = File(context.filesDir, jsonFileName)
        val fis = FileInputStream(file)
        val reader = BufferedReader(InputStreamReader(fis))
        val stringBuilder = StringBuilder()
        var line : String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append("\n")
        }
        val fileContent = stringBuilder.toString()
        cityDataList = gson.fromJson(fileContent, Array<CityData>::class.java)

        Log.d(tag, "get city data list done and send event bus")
        EventBus.getDefault().post(CityDataListReadyEvent(cityDataList.toList()))

    } catch (e: IOException) {
        e.printStackTrace()
    }

    @Suppress("unused")
    fun printCityDataList() {
        for(city in cityDataList) {
            println("city name:${city.name}")
        }
    }
}

