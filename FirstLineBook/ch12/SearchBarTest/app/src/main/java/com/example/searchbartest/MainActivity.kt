package com.example.searchbartest

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        var searchView = findViewById<SearchView>(R.id.searchView)
        var searchBar = findViewById<SearchBar>(R.id.search_bar)
        var result = findViewById<TextView>(R.id.result)

        searchBar.apply {
            setOnClickListener {
                searchView.show()
                searchView.requestFocus()
            }
        }

        searchView.editText.setOnEditorActionListener { v, actionId, event ->
            searchBar.setText(searchView.text)
            searchView.hide()
            result.text = searchView.text
            false
        }
    }
}