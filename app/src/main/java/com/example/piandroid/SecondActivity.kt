package com.example.piandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.piandroid.MainActivity.Companion.INTENT_KEY
import com.example.piandroid.MainActivity.Companion.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        text1.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra(INTENT_KEY, text1.text)
            setResult(REQUEST_CODE, toMainIntent)
            finish()
        }
        text2.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra(INTENT_KEY, text2.text)
            setResult(REQUEST_CODE, toMainIntent)
            finish()
        }
        text3.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra(INTENT_KEY, text3.text)
            setResult(REQUEST_CODE, toMainIntent)
            finish()
        }
        text4.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra(INTENT_KEY, text4.text)
            setResult(REQUEST_CODE, toMainIntent)
            finish()
        }
        text5.setOnClickListener {
            val toMainIntent = Intent(this, MainActivity::class.java)
            toMainIntent.putExtra(INTENT_KEY, text5.text)
            setResult(REQUEST_CODE, toMainIntent)
            finish()
        }
    }
}