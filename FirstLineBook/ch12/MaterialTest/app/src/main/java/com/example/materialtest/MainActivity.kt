package com.example.materialtest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.materialtest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding

    val fruits = mutableListOf(Fruit("Apple", R.drawable.apple), Fruit("Banana",
    R.drawable.banana), Fruit("Orange", R.drawable.orange), Fruit("Watermelon",
    R.drawable.watermelon), Fruit("Pear", R.drawable.pear), Fruit("Grape",
    R.drawable.grape), Fruit("Pineapple", R.drawable.pineapple), Fruit("Strawberry",
    R.drawable.strawberry), Fruit("Cherry", R.drawable.cherry), Fruit("Mango",
    R.drawable.mango))

    val fruitList = ArrayList<Fruit>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding.navView.setCheckedItem(R.id.navCall)
        binding.navView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawers()
            true
        }
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo") {
                    Toast.makeText(this, "Data restored", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
        initFruits()
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FruitAdapter(this, fruitList)
        binding.recyclerView.adapter = adapter

        binding.swipRefresh.setColorSchemeColors(com.google.android.material.R.color.design_default_color_primary)
        binding.swipRefresh.setOnRefreshListener {
            refreshFruits(adapter)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshFruits(adapter: FruitAdapter) {
        thread {
            Thread.sleep(200)
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                binding.swipRefresh.isRefreshing = false
            }
        }
    }

    private fun initFruits() {
        fruitList.clear()
        repeat(50) {
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backup -> Toast.makeText(this, "You clicked Backup",
                Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "You clicked Delete",
                Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "You clicked Settings",
                Toast.LENGTH_SHORT).show()
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
}