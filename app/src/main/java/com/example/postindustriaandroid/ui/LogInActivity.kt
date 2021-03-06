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
        SharedPrefsManager.init(applicationContext)
        super.onCreate(savedInstanceState)
        if (SharedPrefsManager.getTheme())
            setTheme(R.style.Theme_PostindustriaAndroid_Dark)
        setContentView(R.layout.activity_log_in)
        logIn_btn.setOnClickListener(this)
        db = PhotoRoomDatabase.getDatabase(applicationContext)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onClick(v: View?) {
        if (loIn_ET.text?.isNotEmpty() == true) {
            checkUserLogIn(loIn_ET.text.toString())
        } else {
            Toast.makeText(this, getString(R.string.empty_login_field), Toast.LENGTH_SHORT).show()
        }
    }

    fun checkUserLogIn(userName: String){
        val intent = Intent(this, BaseActivity::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            if (db?.userDao()?.getUser(userName) == null){
                db?.userDao()?.insert(UserEntity(0, userName))
            }
            val user: UserEntity = db!!.userDao().getUser(userName)
            saveLastUser(userName, user.id)
        }
        startActivity(intent)
    }

    private fun saveLastUser(login: String, user_id: Long) {
        SharedPrefsManager.saveLogin(login)
        SharedPrefsManager.saveUserId(user_id)
    }
}