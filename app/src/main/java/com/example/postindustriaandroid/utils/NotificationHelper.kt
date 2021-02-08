package com.example.postindustriaandroid.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.ui.BaseActivity

object NotificationHelper {

    private const val CHANNEL_ID = "Upload"

    fun updatePhotosNotification(context: Context, total: String, image: Bitmap) {
        createNotificationChannel(context)

        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(BaseActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.uploadFragment)
            .createPendingIntent()

        val builder =  NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cloud_download_24px)
            .setContentTitle(context.getString(R.string.newPhotosUpdated))
            .setLargeIcon(image)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(image)
                .bigLargeIcon(null))
            .setContentText("$total photos upload")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Background data update",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(serviceChannel)
            }
        }
    }
}