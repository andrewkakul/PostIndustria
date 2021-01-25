package com.example.postindustriaandroid.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.DeleteFileListener
import com.example.postindustriaandroid.data.adapters.SwipeToDeleteDownloadCard
import com.example.postindustriaandroid.data.adapters.files.DownloadFilesAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FilesEntity
import com.example.postindustriaandroid.data.viewmodel.FilesViewModel
import com.example.postindustriaandroid.utils.FileManager
import com.example.postindustriaandroid.utils.SharedPrefsManager
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class GalleryFragment : Fragment(), DeleteFileListener {

    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1001
    }
    private lateinit var db: PhotoRoomDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FilesViewModel
    private val photoAdapter = DownloadFilesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FileManager.init(activity?.applicationContext!!)
        db = PhotoRoomDatabase.getDatabase(activity?.applicationContext!!)
        recyclerView = gallery_RV

        initViewModel()

        to_camera_btn.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun initViewModel(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = photoAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteDownloadCard(photoAdapter, this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel = ViewModelProvider(this).get(FilesViewModel::class.java)
        viewModel.filesLiveData.observe(viewLifecycleOwner,{
            photoAdapter.setData(it)
        })
        viewModel.getListOfFiles(db, SharedPrefsManager.getUserID())
    }

    override fun deleteFileFromDB(filesEntity: FilesEntity) {
        File(filesEntity.uri.toUri().path).delete()
        lifecycleScope.launch(Dispatchers.IO){
            db.filesDao().delete(filesEntity)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
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
                            .start(context?.applicationContext!!, this)
                    }
                }
            }
                UCrop.REQUEST_CROP -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        FileManager.deleteCacheFile()
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.filesDao().insert(FilesEntity(0, FileManager.cropPhotoUri.toString(), SharedPrefsManager.getUserID()))
                            viewModel.getListOfFiles(db, SharedPrefsManager.getUserID())
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
}