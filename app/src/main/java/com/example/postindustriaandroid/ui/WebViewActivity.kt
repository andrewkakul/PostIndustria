package com.example.postindustriaandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.postindustriaandroid.R
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : AppCompatActivity() {

    companion object{
        val PHOTOURL = "photoUrlKey"
        val SEARCHTEXT = "searchTextKey"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        text_webview_activity.text = intent.getStringExtra(SEARCHTEXT)
        webview.loadUrl(intent.getStringExtra(PHOTOURL).toString())
    }
}