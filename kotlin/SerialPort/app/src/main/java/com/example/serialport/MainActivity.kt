package com.example.serialport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener { sendCommand() }

        SerialPortInterface.init("/dev/ttyS0", 115200)
    }

    fun sendCommand() {
        Log.i("test", "myTest")
//        SerialPortInterface.firmwarePortSender.getFirmwareVersion()
    }

}