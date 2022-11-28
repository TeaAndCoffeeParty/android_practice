package com.example.serialport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val getVersionButton = findViewById<Button>(R.id.getVersion)

        getVersionButton.setOnClickListener {
            SerialPortInterface.firmwarePortSender.getFirmwareVersion()
        }
        findViewById<Button>(R.id.singleMove).setOnClickListener {
            val testConfig = mutableListOf<Int>(125, 1, 50, 5000, 1, 50, 125, 500, 48000, 500)
            SerialPortInterface.firmwarePortSender.singleMove(1, testConfig)
        }
        findViewById<Button>(R.id.printUp).setOnClickListener {
            val testPrintUpConfig = mutableListOf<Int>(125,1,40,2000,1,40,4000,1,40,6000,1,40,125)
            val testMoveUp = mutableListOf<Int>(200,500,200,1000,500,8000,800)
            SerialPortInterface.firmwarePortSender.setPrintMoveUpSpeed(1 ,testPrintUpConfig)
            SerialPortInterface.firmwarePortSender.printMoveUp(1, testMoveUp)
        }
        findViewById<Button>(R.id.printDown).setOnClickListener {
            var testPrintDownConfig = mutableListOf<Int>(125,1,40,6000,1,40,4000,1,40,2000,1,40,125)
            var testMoveDown = mutableListOf<Int>(800,2000,500,3200,200,4000,200)
            SerialPortInterface.firmwarePortSender.setPrintMoveDownSpeed(1, testPrintDownConfig)
            SerialPortInterface.firmwarePortSender.printMoveDown(1, testMoveDown)
        }
        findViewById<Button>(R.id.getPosition).setOnClickListener {
            SerialPortInterface.firmwarePortSender.getPrintPosition(1)
        }

        SerialPortInterface.init("/dev/ttyS0", 115200)
    }

}