package com.example.postindustriaandroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.database.PhotoViewModel
import com.example.postindustriaandroid.data.database.PhotoViewModelFactory
import com.example.postindustriaandroid.data.database.PhotosApplication
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.coroutines.launch

class WebViewActivity : AppCompatActivity() {

    private val photoViewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((application as PhotosApplication).repository)
    }

    companion object{
        const val PHOTOURL = "photoUrlKey"
        const val SEARCHTEXT = "searchTextKey"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        text_webview_activity.text = intent.getStringExtra(SEARCHTEXT)
        webview.loadUrl(intent.getStringExtra(PHOTOURL).toString())

        chose_to_favorite_btn.setOnClickListener {
            val photoUrl: String = intent.getStringExtra(PHOTOURL).toString()
            val searchText: String = intent.getStringExtra(SEARCHTEXT).toString()
            val favouritePhotoEntity = FavouritePhotoEntity(0,0, photoUrl, searchText)
            photoViewModel.insert(favouritePhotoEntity)
        }
    }
}