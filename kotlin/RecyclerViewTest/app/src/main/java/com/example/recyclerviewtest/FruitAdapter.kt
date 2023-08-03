package com.example.listviewtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewtest.R

class FruitAdapter(private val fruitList: List<Fruit>) :
    RecyclerView.Adapter<FruitAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fruitImage: ImageView = view.findViewById(R.id.fruitImage)
        val fruitName: TextView = view.findViewById(R.id.fruitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fruit_item, parent, false)
        val viewHolder = ViewHolder(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            println("position:${position}")
            val fruit = fruitList[position]
            Toast.makeText(holder.itemView.context, "you clicked view ${fruit.name}",
                Toast.LENGTH_SHORT).show()
        }
        holder.fruitImage.setOnClickListener {
            val position = holder.bindingAdapterPosition
            println("position:${position}")
            val fruit = fruitList[position]
            Toast.makeText(holder.itemView.context, "you clicked image ${fruit.name}",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = fruitList.size
}