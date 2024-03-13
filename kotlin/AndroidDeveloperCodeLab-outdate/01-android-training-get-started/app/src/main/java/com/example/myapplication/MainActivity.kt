package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var diceImage: ImageView
    lateinit var diceImage1: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        diceImage = findViewById<ImageView>(R.id.dice_image)
        diceImage1 = findViewById<ImageView>(R.id.dice_image1)
        val rollButton: Button = findViewById<Button>(R.id.roll_button)
        rollButton.setOnClickListener { rollDice() }

        findViewById<Button>(R.id.clear_button).setOnClickListener { cleanDicImage() }

    }

    private fun rollDice() {
        Toast.makeText(this, "button clicked",
            Toast.LENGTH_SHORT).show()
//        val drawableResource = when((1..6).random()) {
//            1 -> R.drawable.dice_1
//            2 -> R.drawable.dice_2
//            3 -> R.drawable.dice_3
//            4 -> R.drawable.dice_4
//            5 -> R.drawable.dice_5
//            else -> R.drawable.dice_6
//        }
//        diceImage?.setImageResource(drawableResource)
        diceImage.setImageResource(getRandomDiceImage())
        diceImage1.setImageResource(getRandomDiceImage())
    }

    private fun getRandomDiceImage() : Int {
        return  when((1..6).random()) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }

    private fun cleanDicImage() {
        diceImage.setImageResource(R.drawable.empty_dice)
        diceImage1.setImageResource(R.drawable.empty_dice)
    }
}