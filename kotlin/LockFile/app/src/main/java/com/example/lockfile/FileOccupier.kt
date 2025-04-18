package com.example.lockfile

import java.io.File
import java.io.RandomAccessFile

class FileOccupier {
    private var randomAccessFile : RandomAccessFile? = null

    fun occupyFile(filePath: String) {
        val file = File(filePath)
        try {
            randomAccessFile = RandomAccessFile(file, "rw")
            println("File occupied: $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun releaseFile() {
        try {
            randomAccessFile?.close()
            println("File released.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}