package com.example.fragmentbestpractice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.w3c.dom.Text

class NewsContentFragment : Fragment() {
    lateinit var contentLayout: LinearLayout
    lateinit var newsTitle: TextView
    lateinit var newsContent: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?) : View? {
        contentLayout =  requireActivity().findViewById<LinearLayout>(R.id.contentLayout)
        newsTitle = requireActivity().findViewById<TextView>(R.id.newsTitle)
        newsContent = requireActivity().findViewById<TextView>(R.id.newsContent)
        return inflater.inflate(R.layout.news_content_frag, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun refresh(title: String, content: String) {
        contentLayout.visibility = View.VISIBLE
        newsTitle.text = title
        newsContent.text = content
    }
}