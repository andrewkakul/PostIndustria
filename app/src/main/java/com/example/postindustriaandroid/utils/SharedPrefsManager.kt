package com.example.postindustriaandroid.utils

import android.content.Context

object SharedPrefsManager {

    private const val APP_PREFERENCES = "appPreferences"
    private const val LOGIN_KEY = "loginKey"

    private lateinit var context: Context
    fun init(context: Context) {
        this.context = context
    }

    fun saveLogin(login: String) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().apply {
            this.putString(LOGIN_KEY, login)
            this.apply()
        }
    }

    fun getLogin(): String {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(LOGIN_KEY, null) ?: ""
    }


}