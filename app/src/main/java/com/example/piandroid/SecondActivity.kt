package com.example.piandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        text1.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra("text", text1.text)
            setResult(1, toMainIntent)
            finish()
        }
        text2.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra("text", text2.text)
            setResult(1, toMainIntent)
            finish()
        }
        text3.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra("text", text3.text)
            setResult(1, toMainIntent)
            finish()
        }
        text4.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra("text", text4.text)
            setResult(1, toMainIntent)
            finish()
        }
        text5.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra("text", text5.text)
            setResult(1, toMainIntent)
            finish()
        }
    }
}