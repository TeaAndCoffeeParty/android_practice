package com.example.serialport

object SerialPort {

    fun initSerialPort(
        address: String,
        baudRate: Int,
    ) {
        SerialHandle.init(address, baudRate)
    }
}