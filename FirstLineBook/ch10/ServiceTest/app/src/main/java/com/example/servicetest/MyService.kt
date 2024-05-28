package com.example.servicetest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {
    val TAG = javaClass.simpleName
    private  val mBinder = DownloadBinder()
    class DownloadBinder : Binder() {
        val TAG = javaClass.simpleName
        fun startDownload() {
            Log.d(TAG, "startDownload executed")
        }

        fun getProgress() : Int {
            Log.d(TAG, "getProgress executed")
            return 0
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate executed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand executed")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy executed")
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }
}