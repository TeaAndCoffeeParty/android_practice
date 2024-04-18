package com.example.searchbartest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.searchbartest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.searchView.editText.setOnEditorActionListener { v, actionId, event ->
            binding.searchBar.setText(binding.searchView.text)
            binding.result.text = binding.searchView.text
            false
        }

        binding.searchView.editText.doOnTextChanged { text, start, before, count ->
             Toast.makeText(applicationContext, "$text,$start, $before, $count", Toast.LENGTH_SHORT).show()
        }
    }
}