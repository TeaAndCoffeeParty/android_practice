package com.example.serialport

val cmdHeader = mutableListOf<Byte>((0xC9).toByte(), (0xC8).toByte())
    val cmdEnd = mutableListOf<Byte>((0x0D).toByte(), (0x0A).toByte())
    val checkBitLength = 2

    const val cmdGetVersion = 0x00.toByte()
    const val cmdHeartBeat = 0x01.toByte()
    const val cmdGotoZero = 0x02.toByte()
    const val cmdSingleMove = 0x03.toByte()
    const val cmdSetPrintMoveUpSpeed = 0x04.toByte()
    const val cmdSetPrintMoveDownSpeed = 0x05.toByte()
    const val cmdGetPrintPosition = 0x13.toByte()
    const val cmdProjectorSetIntensity = 0x08.toByte()
    const val cmdProjectorGetIntensity = 0x09.toByte()
    const val cmdPrintMoveUp = 0x0E.toByte()
    const val cmdPrintMoveDown = 0x0F.toByte()
    const val cmdStop = 0x11.toByte()
    const val cmdResinTankHeat = 0x0B.toByte()
    const val cmdPlatformHeat = 0x0D.toByte()
    const val cmdUltrasound = 0x07.toByte()
    const val cmdWeight = (0x12).toByte()
interface FirmwareProtocol {


    fun sendSerialData(func: Byte, data: ByteArray = byteArrayOf()) { }
    fun getFirmwareVersion() {}
    fun getHeartbeat() {}
    fun goToTop(motorIndex:Int, speed: Int) {}
    fun goToBottom(motorIndex:Int, speed: Int) {}
    fun singleMove(motorIndex: Int, config: MutableList<Int>) {}
    fun getPrintPosition(intensity: Int) {}
    fun setPrintMoveUpSpeed(motorIndex: Int, config: MutableList<Int>) {}
    fun setPrintMoveDownSpeed(motorIndex: Int, config: MutableList<Int>) {}
    fun stop(motorIndex: Int) {}
    fun getPosition();
    fun printMoveUp(axis: Int, config: MutableList<Int>) {}
    fun printMoveDown(axis: Int, config: MutableList<Int>) {}
    fun openPlatformHeat(temperature: Int) {}
    fun closePlatformHeat() {}
    fun openResinTankHeat(temperature: Int) {}
    fun closeResinTankHeat() {}
    fun close() {}
}