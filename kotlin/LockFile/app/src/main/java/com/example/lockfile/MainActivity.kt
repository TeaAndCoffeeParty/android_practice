package com.example.lockfile

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blankj.utilcode.util.FileIOUtils
import com.example.lockfile.databinding.ActivityMainBinding
import java.io.File
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isOccupied = false
    private val filePath = Environment.getExternalStorageDirectory().absolutePath.plus("/cureData/test.txt")
    private var fileOccupier = FileOccupier()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val thread1 = Thread {
                for(i in 1..5) {
                    FileIOUtils.writeFileFromString(filePath, "Thread 1 Data $i\n")
                    println("Thread 1 write: $i")
                    Thread.sleep(100)
                }
            }
            val thread2 = Thread {
                for(i in 1..5) {
                    FileIOUtils.writeFileFromString(filePath, "Thread 2 Data $i\n")
                    println("Thread 2 write: $i")
                    Thread.sleep(100)
                }
            }
            thread1.start()
            thread2.start()

            thread1.join()
            thread2.join()
            println("\nFinal file content:")
            File(filePath).forEachLine { println(it) }
        }
    }

    private fun writeContent(filePath: String, content: String) {
        FileIOUtils.writeFileFromString(filePath, content)
    }
}