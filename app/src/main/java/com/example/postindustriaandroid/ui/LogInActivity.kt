package com.example.postindustriaandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase_Impl
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        logIn_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(loIn_ET.text.isNotEmpty()) {
            checkUserLogIn(loIn_ET.text.toString())
        }else{
            Toast.makeText(this, "Login field is required!", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkUserLogIn(userName: String){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}