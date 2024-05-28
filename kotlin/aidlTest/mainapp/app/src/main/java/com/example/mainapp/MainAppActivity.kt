package com.example.mainapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mainapp.databinding.ActivityMainBinding

class MainAppActivity : AppCompatActivity() {
    private val TAG = "AIDL-MainAppActivity"
    lateinit var binding : ActivityMainBinding
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
        Log.v(TAG, "onCreate()")
//        val intent = Intent(this, MainAppService::class.java)
//        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy()")
//        val intent = Intent(this, MainAppService::class.java)
//        stopService(intent)
    }
}