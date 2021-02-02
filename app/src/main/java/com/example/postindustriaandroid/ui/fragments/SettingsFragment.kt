package com.example.postindustriaandroid.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.utils.SharedPrefsManager
import com.example.postindustriaandroid.workers.UploadWorker
import java.util.concurrent.TimeUnit


class SettingsFragment : PreferenceFragmentCompat(){

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var text: String
    private var upload: Boolean = false
    private var period: Int = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPause() {
        super.onPause()
        upload = sharedPreferences.getBoolean("upload", false)
        text = sharedPreferences.getString("search_text", "").toString()
        period = Integer.parseInt(sharedPreferences.getString("period", "0"))

        val uploadWorkRequest =
            PeriodicWorkRequestBuilder<UploadWorker>(period.toLong(), TimeUnit.MINUTES)
                .build()
        val workManager = WorkManager.getInstance(requireContext())
        if(upload && text.isNotEmpty()) {
            workManager.enqueueUniquePeriodicWork(
                "upload_photo",
                ExistingPeriodicWorkPolicy.REPLACE,
                uploadWorkRequest
            )
        }else {
            sharedPreferences.edit().putBoolean("upload", false).apply()
            workManager.cancelWorkById(uploadWorkRequest.id)
        }

        SharedPrefsManager.saveTheme(sharedPreferences.getBoolean("night_theme", false))
        activity?.recreate()
    }
}