package com.example.postindustriaandroid.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.UploadEntity
import com.example.postindustriaandroid.data.model.Photo
import com.example.postindustriaandroid.utils.NetworkManager
import com.example.postindustriaandroid.utils.NotificationHelper
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.net.URL

class UploadWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object{
        const val KEY_UPLOAD_TEXT = "key_upload_text"
    }

    val TAG = "UPLOAD_WORKER"

    override fun doWork(): Result {
        return try {
            GlobalScope.launch(Dispatchers.IO) {
                val service = NetworkManager.createService()
                val data = NetworkManager.createData(inputData.getString(KEY_UPLOAD_TEXT).toString())
                val call = service.getPhoto(data)
                call.awaitResponse().body()!!.photos.photo.let { photoList ->
                    saveUploadPhoto(photoList)
                    val url = URL(photoList[0].generateUrl())
                    val image: Bitmap = BitmapFactory.decodeStream(url.openStream())
                    NotificationHelper.updatePhotosNotification(context, photoList.size.toString(), image)
                }
            }
            Result.success()
        } catch (throwable: Throwable){
            Log.e(TAG, throwable.toString())
            Result.failure()
        }
    }

    private fun saveUploadPhoto(listOfPhoto: List<Photo>){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val text = sharedPreferences.getString("search_text", "").toString()
        val db: PhotoRoomDatabase = PhotoRoomDatabase.getDatabase(applicationContext)
        val user_id = SharedPrefsManager.getUserID()
        listOfPhoto.forEach {
            val uploadEntity = UploadEntity(0, user_id, it.generateUrl(), text)
            db.uploadDao().insert(uploadEntity)
        }
    }
}