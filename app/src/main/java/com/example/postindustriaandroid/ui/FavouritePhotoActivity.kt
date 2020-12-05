package com.example.postindustriaandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.FavoritePhotoCardAdapter
import com.example.postindustriaandroid.data.adapters.HistoryAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.viewmodel.FavouriteViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_favourite_photo.*


class FavouritePhotoActivity : AppCompatActivity() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: FavoritePhotoCardAdapter
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_photo)
        db = PhotoRoomDatabase.getDatabase(this)
        SharedPrefsManager.init(this)
        recyclerView = favouritePhoto_RV
        initViewModel()
    }

    private fun initViewModel() {
        val userId = intent.getLongExtra(WebViewActivity.USERID, -1)
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        viewModel.favoriteLiveData.observe(this, {
            photoAdapter = FavoritePhotoCardAdapter(it)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = photoAdapter
        })
        viewModel.getListOfPhoto(userId, db)
    }
}