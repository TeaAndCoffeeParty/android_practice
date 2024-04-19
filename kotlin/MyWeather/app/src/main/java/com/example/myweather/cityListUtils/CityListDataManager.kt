package com.example.myweather.cityListUtils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.GZIPInputStream

class CityListDataManager(private val context: Context) {
    private val tag = "CityListDataManager"
    private val jsonFileName = "city.list.json"
    private val cityListJsonFile : File = File(context.filesDir, jsonFileName)
    init {
        if(!isExistCityListJsonFile(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                unzipGzFile(context)
            }
        }
    }

    private fun isExistCityListJsonFile(context: Context) : Boolean {
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
}