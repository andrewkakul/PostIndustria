package com.example.postindustriaandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.FavoritePhotoCardAdapter
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.viewmodel.FavouriteViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.activity_favourite_photo.*
import kotlinx.coroutines.launch


class FavouritePhotoActivity : AppCompatActivity() {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var recyclerView: RecyclerView
    private var photoAdapter = FavoritePhotoCardAdapter()
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_photo)
        Log.d("Test", "q----")
        db = PhotoRoomDatabase.getDatabase(this)
        SharedPrefsManager.init(this)
        Log.d("Test", "1----")
        val userId = intent.getLongExtra(WebViewActivity.USERID, -1)
        Log.d("Test", "2----")
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        Log.d("Test", "3----")
        viewModel.favoriteLiveData.observe(this, { favoriteList ->
            lifecycleScope.launch {
                Log.d("Test", "4----")
                photoAdapter.setData(favoriteList)
                Log.d("Test", "5----")
            }
        })
        viewModel.getListOfPhoto(userId, db)

        recyclerView = favouritePhoto_RV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = photoAdapter
    }



}