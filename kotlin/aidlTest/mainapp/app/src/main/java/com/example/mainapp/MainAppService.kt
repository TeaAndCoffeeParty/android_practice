package com.example.mainapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MainAppService : Service() {
    private val TAG = "AIDL-MainAppService"
    private val mStub = AppAidlInterfaceStub()
    inner class AppAidlInterfaceStub : IAppAidlInterface.Stub() {
        var mStrData = ""
        var mSetServiceRunning = true
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {

        }

        override fun setStringData(strData: String?) {
            mStrData = strData.toString()
        }

    }

    override fun onCreate() {
        Log.v(TAG, "onCreate()")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        mStub.mSetServiceRunning = true
        Thread {
            while(mStub.mSetServiceRunning) {
                try {
                    Thread.sleep(1000);
                    Log.v(TAG, "mStrData:${mStub.mStrData}")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        return mStub
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(TAG, "onUnbind()")
        mStub.mSetServiceRunning = false
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy()")
    }
}