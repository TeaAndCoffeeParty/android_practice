package com.example.serialport

interface SerialState {
    fun firmwareVersion(firmwareVersion: String)
    fun onFinishGoToTop(position: String)
    fun onFinishGoToBottom(position: String)
    fun onFinishSingleMOve(position: String)
    fun onFinishPrintMoveUp(position: String)
    fun onFinishPrintMoveDown(position: String)
    fun onFinishGetPrintPosition(position: String)
    fun heartBeat(heartBeat: String)
}