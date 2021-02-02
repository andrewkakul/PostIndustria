package com.example.postindustriaandroid.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

object BatteryReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent?.getIntExtra("level", 0)
        Toast.makeText(context, "Battery charge ${level.toString()}%", Toast.LENGTH_SHORT).show()
    }
}