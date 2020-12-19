package com.example.postindustriaandroid.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object  FileManager {

    private const val FILE_PROVIDER_AUTHORITY = "com.example.postindustriaandroid.fileprovider"

    private lateinit var context: Context
    var tempFile: File? = null
    var photoUri: Uri? = null
    var cropPhotoUri: Uri? = null

    fun init(context: Context){
        this.context = context
    }

    fun createImageFile(){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = File(context.cacheDir.absolutePath + "/Pictures")
        storageDir?.mkdirs()
        tempFile = File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir) /* directory */
    }

    fun getUri(): Uri? {
        tempFile?.also {
                photoUri = FileProvider.getUriForFile(
                    context
                    ,FILE_PROVIDER_AUTHORITY
                    ,it)
        }
        return  photoUri
    }

    fun getCropUri(): Uri{
        val storageDir = File(context.filesDir.absolutePath + "/Pictures")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        storageDir.mkdirs()
        cropPhotoUri = Uri.fromFile(File(storageDir, "${timeStamp}.jpg"))
        return cropPhotoUri!!
    }

    fun deleteCacheFile() {
        tempFile?.let {
            it.delete()
            photoUri = null
        }
    }

    fun deleteCropFile() {
        val file = File(cropPhotoUri?.path)
        file.delete()
    }
}