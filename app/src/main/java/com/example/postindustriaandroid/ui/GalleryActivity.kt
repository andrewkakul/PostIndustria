package com.example.postindustriaandroid.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.DeleteFileListener
import com.example.postindustriaandroid.data.adapters.files.DownloadFilesAdapter
import com.example.postindustriaandroid.data.adapters.SwipeToDeleteDownloadCard
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FilesEntity
import com.example.postindustriaandroid.data.viewmodel.FilesViewModel
import com.example.postindustriaandroid.utils.FileManager
import com.example.postindustriaandroid.utils.SharedPrefsManager
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class GalleryActivity : AppCompatActivity(), DeleteFileListener {

    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1001
    }

    private lateinit var db: PhotoRoomDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FilesViewModel
    private val photoAdapter = DownloadFilesAdapter()
    private var userId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefsManager.getTheme() == MainActivity.NIGHT)
            setTheme(R.style.Theme_PostindustriaAndroid_Dark)
        setContentView(R.layout.activity_gallery)
        FileManager.init(applicationContext)
        db = PhotoRoomDatabase.getDatabase(applicationContext)
        recyclerView = gallery_RV

        initViewModel()

        to_camera_btn.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun initViewModel(){
        userId = intent.getLongExtra(WebViewActivity.USERID, -1)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = photoAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteDownloadCard(photoAdapter, this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel = ViewModelProvider(this).get(FilesViewModel::class.java)
        viewModel.filesLiveData.observe(this,{
            photoAdapter.setData(it)
        })
        viewModel.getListOfFiles(db, userId!!)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                try {
                    FileManager.createImageFile()
                } catch (ex: IOException) {
                    Log.d("Gallery", "Fail create", ex)
                    return
                }
                val photoURI = FileManager.getUri()
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    FileManager.photoUri?.let {
                        UCrop.of(it, FileManager.getCropUri())
                            .start(this)
                    }
                }
            }
            UCrop.REQUEST_CROP -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        FileManager.deleteCacheFile()
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.filesDao().insert(FilesEntity(0, FileManager.cropPhotoUri.toString(), userId!!))
                            viewModel.getListOfFiles(db, userId!!)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        FileManager.deleteCropFile()
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun deleteFileFromDB(filesEntity: FilesEntity) {
        File(filesEntity.uri.toUri().path).delete()
        lifecycleScope.launch(Dispatchers.IO){
            db.filesDao().delete(filesEntity)
        }
    }
}