package com.example.postindustriaandroid.utils
import android.content.Context

object SharedPrefsManager {

    private const val APP_PREFERENCES = "appPreferences"
    private const val LOGIN_KEY = "loginKey"
    private const val USERID_KEY = "useridKey"
    private const val HISTORY_REQUEST_KEY = "searchKey"
    private const val THEME_KEY = "themekey"

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

    fun saveUserId(user_id: Long){
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().apply {
            this.putLong(USERID_KEY, user_id)
            this.apply()
        }
    }

    fun getUserID(): Long {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getLong(USERID_KEY, 0)
    }

    fun saveHistory(search: String) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().apply {
            this.putString(HISTORY_REQUEST_KEY, search)
            this.apply()
        }
    }
    fun getHistory(): String {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(HISTORY_REQUEST_KEY, null) ?: ""
    }
    fun saveTheme(nightTheme: Boolean?){
        context.getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).edit().apply(){
            this.putBoolean(THEME_KEY, nightTheme!!)
            this.apply()
        }
    }
    fun getTheme(): Boolean{
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getBoolean(THEME_KEY, false)
    }
}