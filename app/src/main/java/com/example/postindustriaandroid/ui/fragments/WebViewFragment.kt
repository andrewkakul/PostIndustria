package com.example.postindustriaandroid.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.example.postindustriaandroid.data.viewmodel.BaseViewModel
import com.example.postindustriaandroid.data.viewmodel.PreviewFavouriteViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WebViewFragment : Fragment() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var viewModel: PreviewFavouriteViewModel

    lateinit var model: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = activity?.applicationContext?.let { PhotoRoomDatabase.getDatabase(it) }!!
        activity?.applicationContext?.let { SharedPrefsManager.init(it) }
        val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false) as PreviewFavouriteDataBinding
        model = ViewModelProvider(requireActivity()).get(BaseViewModel::class.java)
        initViewModel()
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_webview_activity.text = model.search_text
        webview.loadUrl(model.photo_url)
        download_image_btn.setOnClickListener {
            downloadImage()
        }
    }

     private fun initViewModel(){
         viewModel = ViewModelProvider(this).get(PreviewFavouriteViewModel::class.java)
         viewModel.isFavorite.observe(viewLifecycleOwner, {
             viewModel.saveData(model.photo_url, model.search_text, db, SharedPrefsManager.getUserID())
         })
         viewModel.loadData(model.photo_url, SharedPrefsManager.getUserID(), db)
     }

    fun downloadImage(){
        if (ContextCompat.checkSelfPermission(
                context?.applicationContext!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context?.applicationContext!!,
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
            .load(model.photo_url)
            .addListener(object: RequestListener<File> {
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
                    val dir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val file = File(dir, "${timeStamp}.jpg")
                    resource?.copyTo(file)
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.filesDao().insert(FilesEntity(0, Uri.fromFile(file).toString(), SharedPrefsManager.getUserID()))
                    }
                    return false
                }
            })
            .submit()
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