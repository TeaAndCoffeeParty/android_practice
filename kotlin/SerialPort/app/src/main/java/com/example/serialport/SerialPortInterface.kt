package com.example.serialport

import android.serialport.SerialPort
import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import kotlin.collections.*

internal object SerialPortInterface {
    private const val TAG = "SerialPort"
    private var tryCount = 1
    val firmwarePortSender = FirmwarePortSender()
    private var buff = byteArrayOf()
    private var listener : SerialState? = null
    private lateinit var serialHelper: SerialPort
    private lateinit var readJob: Job
    private lateinit var heartJob: Job

    fun init(address:String, baudRate:Int) {
        openSerialPort(address, baudRate)
    }

    private fun openSerialPort(address:String, baudRate: Int) {
        try {
            SerialPort.setSuPath("/System/xbin/su")
            val file = File(address)
            serialHelper = SerialPort(file, baudRate)
            FirmwarePortSender.outputStream = serialHelper.outputStream
            receivedSerialData(BufferedInputStream(serialHelper.inputStream))
            startHeartbeat()
        } catch (e: Exception) {
            tryCount++
            if(tryCount < 50) {
                openSerialPort(address, baudRate)
            } else {
                Log.e(TAG, "open serial failed: ${e.stackTraceToString()}")
            }
        }
    }

    fun closePort() {
        readJob.cancel()
        heartJob.cancel()
        serialHelper.tryClose()
    }

    private fun startHeartbeat() {
        heartJob = CoroutineScope(Dispatchers.Main).launch {
            while(true) {
                firmwarePortSender.getHeartbeat()
                delay(4000)
            }
        }
    }

//    private fun heartBeat() {
//        Log.d(SerialHandle.TAG, "send:${SerialHandle.toHexString(cmd)}")
//        serialSend.sendSerialData(cmdHeartBeat)
//        sendSerialData(cmdHeartBeat)
//    }
//
//    private fun sendSerialData(func: Byte, data: ByteArray = byteArrayOf()) {
//        var cmd = byteArrayOf()
//        cmd += cmdHeader
//        cmd += func
//        val dataSize = data.size
//        val size = dataSize + checkBitLength
//        cmd += size.toByte()
//        if(dataSize > 0) {
//            cmd += data
//        }
//        cmd += checkNum(cmd)
//        cmd += cmdEnd
//        Log.d(TAG, "send:${toHexString(cmd)}")
//        serialSend.send2Port(cmd)
//    }

    private fun receivedSerialData(buffInputStream: BufferedInputStream) {
        readJob = CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                while(true) {
                    delay(1)
                    if(buffInputStream.available() == 0) continue
                    val received = ByteArray(1024)
                    val size = buffInputStream.read(received)
                    if(size <= 0) continue
                    val byteArray = ByteArray(size)
                    System.arraycopy(received, 0, byteArray, 0, size)
                    Log.d(TAG, "array = ${toHexString(byteArray)}")
                    if(checkComplete(buff.plus(byteArray))) {
                        buff = buff.plus(byteArray)
                        decodeSerialData(buff)
                        buff = byteArrayOf()
                    } else {
                        val dataItems = cutupMulti(buff.plus(byteArray))
                        var count = 0
                        if(dataItems.isNotEmpty()) {
                            dataItems.forEach {
                                count += it.size
                                decodeSerialData(it)
                            }
                        }
                        buff = buff.plus(byteArray).drop(count).toByteArray()
                    }
                }
            }
        }
    }



    private fun checkComplete(byteArray: ByteArray) : Boolean {
        val size = byteArray.size
        if(size < 8) return false
        if(headerNotRight(byteArray)) return false
        if(endNotRight(byteArray)) return false
        if(lengthNoRight(byteArray)) return false

        val cmd = ByteArray(size - 4)
        System.arraycopy(byteArray, 0, cmd, 0, size - 4)
        val checkNum = checkNum(cmd)
        if(checkNumNotRight(checkNum, byteArray)) {
            Log.w(TAG, "receive checkSum error: ${toHexString(byteArray)}")
        }
        return true
    }

    private fun headerNotRight(byteArray: ByteArray) = byteArray[0] != 0xC9.toByte() ||
            byteArray[1] != 0xC8.toByte()
    private fun endNotRight(byteArray: ByteArray) = byteArray[byteArray.size - 2] != 0x0D.toByte()
            || byteArray[byteArray.size - 1] != 0x0A.toByte()
    private fun lengthNoRight(byteArray: ByteArray) = byteArray[3].toInt() != (byteArray.size - 6)

    private fun checkNumNotRight(checkNum:ByteArray, byteArray: ByteArray) = checkNum[0] !=
            byteArray[byteArray.size - 4 ] || checkNum[1] != byteArray[byteArray.size - 3]

    private fun checkNum(byteArray: ByteArray) : ByteArray {
        var sum: Long = 0
        byteArray.forEach { sum += it.toLong() and 0xFF }
        sum = ((sum) shr 16) + (sum and 0xffff)
        sum += (sum shr 16)
        val result = sum.toInt().inv()
        val array = mutableListOf<Byte>()
        array.add(((result shr 8) and 0xFF).toByte())
        array.add(((result and 0xFF).toByte()))
        return array.toByteArray()
    }

    private fun decodeSerialData(serialData: ByteArray) {
        var event = ""
        var sendData = ""
        when (serialData[2]) {
            cmdGetVersion -> {}
            cmdGotoZero -> {}
            cmdSingleMove -> {}
            cmdPrintMoveUp -> {}
            cmdPrintMoveDown -> {}
            cmdGetPrintPosition -> {}
            cmdHeartBeat -> { handleHeartBeat(serialData) }
            cmdUltrasound -> {}
            cmdWeight -> {}
            else -> {}
        }
    }

    private fun cutupMulti(byteArray: ByteArray): MutableList<ByteArray> {
        val list = mutableListOf<ByteArray>()
        val size = byteArray.size
        if(size <= 8) return list
        if(headerNotRight(byteArray)) return list
        val completeReceive = ByteArray(byteToInt(byteArray[3]) + 6)
        if(completeReceive.size > byteArray.size) return list
        System.arraycopy(byteArray, 0, completeReceive, 0 , completeReceive.size)
        if(!checkComplete(completeReceive)) return list
        list.add(completeReceive)
        val subList = cutupMulti(byteArray.drop(completeReceive.size).toByteArray())
        if(subList.isNotEmpty()) list.addAll(subList)
        return list
    }

    private fun byteToInt(data: Byte) : Int = data.toInt() and 0xff


    private fun toHexString(byteArray: ByteArray) = with(StringBuilder()) {
        for (byte in byteArray) {
            val hex = byte.toInt() and 0xFF
            val hexStr = Integer.toHexString(hex)
            if (hexStr.length == 1) append("0").append(hexStr)
            else append(hexStr)
        }
        toString().uppercase()
    }

    private fun byteArrayToInt(data: ByteArray) : Int {
        return if (data.size == 2) {
            ((data[0].toInt() and 0xFF) shl 8) or (data[1].toInt() and 0xFF)
        } else {
            (((data[0].toInt() and 0xFF) shl 24) or  ((data[1].toInt() and 0xFF) shl 16) or
                    ((data[2].toInt() and 0xFF) shl 8) or (data[3].toInt() and 0xFF))
        }
    }

    private fun handleHeartBeat(serialData: ByteArray) {
        var event = PortEvent.HeartBeat
        var sendData = ""
        val isPlatformHeating = serialData[4].toInt()
        val isTankHeating = serialData[5].toInt()
        val hasPlatform = serialData[6].toInt()
        val platformTemperature = byteArrayToInt(byteArrayOf(serialData[7], serialData[8]))
        val resinTankTemperature = byteArrayToInt(byteArrayOf(serialData[9], serialData[10]))
        sendData = isPlatformHeating.toString().plus(",").plus(isTankHeating).plus(",")
            .plus(hasPlatform).plus(",").plus(platformTemperature).plus(",")
            .plus(resinTankTemperature).plus(",")
        listener?.heartBeat(sendData)
    }
}