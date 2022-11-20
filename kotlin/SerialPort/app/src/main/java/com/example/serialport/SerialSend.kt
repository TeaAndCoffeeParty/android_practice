package com.example.serialport

import java.io.OutputStream

class SerialSend {
    fun send2Port(data: ByteArray) {
        outputStream?.write(data)
        outputStream?.flush()
    }

    companion object {
        var outputStream: OutputStream? = null
    }
}