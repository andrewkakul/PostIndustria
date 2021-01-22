package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var photo_url = String()
    var search_text = String()

    fun setData(url: String, text: String){
        photo_url = url
        search_text = text
    }
}