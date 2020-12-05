package com.example.postindustriaandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.UserEntity
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity(), View.OnClickListener {

    private var db: PhotoRoomDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        logIn_btn.setOnClickListener(this)
        db = PhotoRoomDatabase.getDatabase(this)
        SharedPrefsManager.init(this)
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
        lifecycleScope.launch(Dispatchers.IO) {
            if(db?.userDao()?.getUser(userName) == null){
                db?.userDao()?.insert(UserEntity(0, userName))
            }
            saveLastUserLogin(userName)
        }
        startActivity(intent)
    }

    private fun saveLastUserLogin(login: String) {
        SharedPrefsManager.saveLogin(login)
    }
}