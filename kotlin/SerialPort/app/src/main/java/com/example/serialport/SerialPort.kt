package com.example.serialport

object SerialPort {

    fun initSerialPort( address: String, baudRate: Int, ) {
        SerialPortInterface.init(address, baudRate)
    }
}