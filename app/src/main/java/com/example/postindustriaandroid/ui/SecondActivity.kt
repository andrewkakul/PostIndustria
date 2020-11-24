package com.example.postindustriaandroid.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.postindustriaandroid.R
import kotlinx.android.synthetic.main.activity_second.*


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val intent = intent
        if (intent == null || intent.data == null) {
            finish()
        }

        val action = intent!!.action
        if (action == Intent.ACTION_VIEW) {
            intent!!.data?.let { openDeepLink(it) }
        }

    }
    fun openDeepLink(data: Uri) {
        var path0 = ""
        if (data.getPathSegments().size >= 1) {
            path0 = data.getPathSegments().get(0)
            webview.loadUrl(data.toString())
        }
        if (path0.isEmpty()) {
        }
    }
}