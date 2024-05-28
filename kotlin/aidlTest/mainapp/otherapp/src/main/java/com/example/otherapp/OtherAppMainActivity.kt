package com.example.otherapp

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mainapp.IAppAidlInterface
import com.example.mainapp.IAppAidlInterface.Stub
import com.example.otherapp.databinding.ActivityOtherAppMainBinding

class OtherAppMainActivity : AppCompatActivity() {
    private val TAG = "AIDL-OtherAppMainActivity"
    private lateinit var binding : ActivityOtherAppMainBinding
    private lateinit var mServiceIntent: Intent
    private var mBinder : IAppAidlInterface? = null
    private var mICount = 0

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if(mBinder == null) {
                mBinder = IAppAidlInterface.Stub.asInterface(service)
                mICount++
                Log.v(TAG, "onServiceConnected() the time $mICount")
                try {
                    val strData = "第 $mICount 次连接service成功"
                    mBinder!!.setStringData(strData)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            } else {
                mICount++
                Log.v(TAG, "mBinder is not null")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.v(TAG, "onServiceDisconnected")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtherAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mServiceIntent = Intent()
        mServiceIntent.component = ComponentName("com.example.mainapp", "com.example.mainapp.MainAppService")
        // 检查服务是否存在
        val resolveInfo = packageManager.resolveService(mServiceIntent, PackageManager.MATCH_DEFAULT_ONLY)

        if (resolveInfo != null) {
            // 服务存在，Intent有效
            Log.v(TAG, "Intent is valid")
        } else {
            // 服务不存在，Intent无效
            Log.v(TAG, "Intent is not valid")
        }

        binding.bindServiceBtn.setOnClickListener {
            val bindResult = bindService(mServiceIntent, connection, Context.BIND_AUTO_CREATE)
            Log.v(TAG, "bindServiceBtn clicked bindResult:$bindResult")
        }
        binding.unbindServiceBtn.setOnClickListener {
            Log.v(TAG, "unbindServiceBtn clicked")
            unbindService(connection)
            mBinder = null
        }
    }

}