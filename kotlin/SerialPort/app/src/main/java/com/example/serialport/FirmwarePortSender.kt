package com.example.serialport

import android.util.Log
import java.io.OutputStream

class FirmwarePortSender : FirmwareProtocol {
    private fun send2Port(data: ByteArray) {
        outputStream?.write(data)
        outputStream?.flush()
    }

    companion object {
        var outputStream: OutputStream? = null
    }

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


    override fun sendSerialData(func: Byte, data: ByteArray) {
        var cmd = byteArrayOf()
        cmd += cmdHeader
        cmd += func
        val dataSize = data.size
        val size = dataSize + checkBitLength
        cmd += size.toByte()
        if(dataSize > 0) {
            cmd += data
        }
        cmd += checkNum(cmd)
        cmd += cmdEnd
        send2Port(cmd)
    }

    override fun getHeartbeat() {
        Log.i("FirmwareProtocol", "send heartbeat");
        sendSerialData(cmdHeartBeat)
    }

    override fun getFirmwareVersion() {
        sendSerialData(cmdGetVersion)
    }

    override fun goToTop(motorIndex: Int, speed: Int) {
        var data = byteArrayOf()
        data += motorIndex.toByte()
        data += 0x01
        data += intToByteArray(speed)
        sendSerialData(cmdGotoZero, data)
    }

    override fun goToBottom(motorIndex: Int, speed: Int) {
        var data = byteArrayOf()
        data += motorIndex.toByte()
        data += 0x02
        data += intToByteArray(speed)
        sendSerialData(cmdGotoZero, data)
    }

    override fun singleMove(motorIndex: Int, config: MutableList<Int>) {
        var data = byteArrayOf()
        data += motorIndex.toByte()
        data += intToByteArray(config[0])
        data += config[1].toByte()
        data += config[2].toByte()
        data += intToByteArray(config[3])
        data += config[4].toByte()
        data += config[5].toByte()
        data += intToByteArray(config[6])
        data += intToByteArray(config[7])
        data += intToByteArray(config[8])
        data += intToByteArray(config[9])
        sendSerialData(cmdSingleMove, data)
    }

    override fun setPrintMoveUpSpeed(motorIndex: Int, config: MutableList<Int>) {
        var data = byteArrayOf()
        data += motorIndex.toByte()
        data += intToByteArray(config[0])
        data += config[1].toByte()
        data += config[2].toByte()
        data += intToByteArray(config[3])
        data += config[4].toByte()
        data += config[5].toByte()
        data += intToByteArray(config[6])
        data += config[7].toByte()
        data += config[8].toByte()
        data += intToByteArray(config[9])
        data += config[10].toByte()
        data += config[11].toByte()
        data += intToByteArray(config[12])
        sendSerialData(cmdSetPrintMoveUpSpeed, data)
    }
    override fun setPrintMoveDownSpeed(motorIndex: Int, config: MutableList<Int>) {
        var data = byteArrayOf()
        data += motorIndex.toByte()
        data += intToByteArray(config[0])
        data += config[1].toByte()
        data += config[2].toByte()
        data += intToByteArray(config[3])
        data += config[4].toByte()
        data += config[5].toByte()
        data += intToByteArray(config[6])
        data += config[7].toByte()
        data += config[8].toByte()
        data += intToByteArray(config[9])
        data += config[10].toByte()
        data += config[11].toByte()
        data += intToByteArray(config[12])
        sendSerialData(cmdSetPrintMoveDownSpeed, data)
    }

    override fun printMoveUp(axis: Int, config: MutableList<Int>) {
        var data = byteArrayOf()
        data += axis.toByte()
        data += intToByteArray(config[0])
        data += intToByteArray(config[1] + axis)
        data += intToByteArray(config[2])
        data += intToByteArray(config[3] + axis)
        data += intToByteArray(config[4])
        data += intToByteArray(config[5] + axis)
        data += intToByteArray(config[6])
        sendSerialData(cmdPrintMoveUp, data)
    }

    override fun printMoveDown(axis: Int, config: MutableList<Int>) {
        var data = byteArrayOf()
        data += intToByteArray(config[0])
        data += intToByteArray(axis - config[1])
        data += intToByteArray(config[2])
        data += intToByteArray(axis - config[3])
        data += intToByteArray(config[4])
        data += intToByteArray(axis - config[5])
        data += intToByteArray(config[6])
        sendSerialData(cmdPrintMoveDown, data)
    }

    override fun getPrintPosition(motorIndex: Int) {
        var data = byteArrayOf()
        data += intToByteArray(motorIndex)
        sendSerialData(cmdGetPrintPosition, data)
    }

    override fun stop(motorIndex: Int) {
        var data = byteArrayOf()
        data += motorIndex.toByte()
        sendSerialData(cmdStop, data)
    }

    override fun getPosition() {
    }

    override fun openPlatformHeat(temperature: Int) {

    }

    override fun closePlatformHeat() {

    }

    override fun openResinTankHeat(temperature: Int) {

    }

    override fun closeResinTankHeat() {

    }

    override fun close() {

    }

    private fun intToByteArray(data: Int): ByteArray {
        val array = mutableListOf<Byte>()
        array.add(((data shr 24) and 0xff).toByte())
        array.add(((data shr 16) and 0xff).toByte())
        array.add(((data shr 8) and 0xff).toByte())
        array.add(((data) and 0xff).toByte())
        return array.toByteArray()
    }
}