package com.example.viewpager2tester

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity){

    private val pages = listOf("Page 1", "Page 2", "Page 3", "Page 4")
    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return PageFragment(position+1)
    }
}