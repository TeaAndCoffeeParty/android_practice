package com.example.myweather.cityListUtils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    init {
        if(!isExistCityListJsonFile(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                unzipGzFile(context)
            }
        } else {
            testGetJsonData(context)
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

    private fun testGetJsonData(context: Context) {
        var file = File(context.filesDir, jsonFileName)
        try {
            val fis = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(fis))
            val stringBuilder = StringBuilder()
            var line : String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
                Log.d(tag, "line:$line")
            }
            val fileContent = stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}