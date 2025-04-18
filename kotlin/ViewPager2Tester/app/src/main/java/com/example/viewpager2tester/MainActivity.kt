package com.example.viewpager2tester

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager2tester.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = ViewPageAdapter(this)
        binding.viewPager.adapter = adapter
        val recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        val sa: SimpleItemAnimator = recyclerView.itemAnimator as SimpleItemAnimator
        sa.supportsChangeAnimations = false
        recyclerView.itemAnimator = null

        TabLayoutMediator(binding.tabIndicator, binding.viewPager) {tab, position ->
            tab.text = "Page ${position + 1}"
        }.attach()

        binding.viewPager.unregisterOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                println("Selected page: $position")
            }
        })
    }
}