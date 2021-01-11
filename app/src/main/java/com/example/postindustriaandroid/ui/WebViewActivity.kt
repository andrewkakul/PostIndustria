package com.example.postindustriaandroid.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.postindustriaandroid.PreviewFavouriteDataBinding
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FilesEntity
import com.example.postindustriaandroid.data.viewmodel.PreviewFavouriteViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class WebViewActivity : AppCompatActivity() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var viewModel: PreviewFavouriteViewModel
    companion object{
        const val PHOTOURL = "photoUrlKey"
        const val SEARCHTEXT = "searchTextKey"
        const val USERID = "userID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefsManager.getTheme() == MainActivity.NIGHT)
            setTheme(R.style.Theme_PostindustriaAndroid_Dark)
        db = PhotoRoomDatabase.getDatabase(this)
        SharedPrefsManager.init(this)
        initViewModel()

        text_webview_activity.text = intent.getStringExtra(SEARCHTEXT)
        webview.loadUrl(intent.getStringExtra(PHOTOURL).toString())

        download_image_btn.setOnClickListener {
            downloadImage()
        }
    }

    private fun initViewModel(){
        val photoUrl: String = intent.getStringExtra(PHOTOURL).toString()
        val searchText: String = intent.getStringExtra(SEARCHTEXT).toString()
        val userId: Long = intent.getLongExtra(USERID, -1)
        viewModel = ViewModelProvider(this).get(PreviewFavouriteViewModel::class.java)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_webview) as PreviewFavouriteDataBinding
        binding.viewmodel = viewModel

        viewModel.isFavorite.observe(this, {
            viewModel.saveData(photoUrl, searchText, db, userId)
        })
        viewModel.loadData(photoUrl, userId, db)
    }

    fun downloadImage(){
        val photoUrl: String = intent.getStringExtra(PHOTOURL).toString()
        val userId: Long = intent.getLongExtra(USERID, -1)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            val array = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            requestPermissions(array, 1)
        }

        Glide.with(this)
            .asFile()
            .load(photoUrl)
            .addListener(object: RequestListener<File>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val file = File(dir, "${timeStamp}.jpg")
                    resource?.copyTo(file)
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.filesDao().insert(FilesEntity(0, Uri.fromFile(file).toString(), userId))
                    }
                    return false
                }
            })
            .submit()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                downloadImage()
            }
        }
    }
}
