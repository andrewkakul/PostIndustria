package com.example.postindustriaandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.*
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_favourite_photo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritePhotoActivity : AppCompatActivity(), OnFavouriteCardListener, DeleteItemListener {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var recyclerView: RecyclerView
    private val photoAdapter = FavoritePhotoCardAdapter(this)
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_photo)
        db = PhotoRoomDatabase.getDatabase(applicationContext)
        recyclerView = favouritePhoto_RV
        initViewModel()
    }

    private fun initViewModel() {
        val userId = intent.getLongExtra(WebViewActivity.USERID, -1)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = photoAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteFavouriteCallback(photoAdapter, this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        viewModel.favoriteLiveData.observe(this,{ favouriteList ->
            photoAdapter.setData(favouriteList)
        })
        viewModel.getListOfPhoto(userId, db)
    }

    override fun onDeleteBtnClick(position: Int, favouritePhotoEntity: FavouritePhotoEntity) {
        photoAdapter.deleteItem(position)
        if (photoAdapter.checkPreviewElement(position))
            photoAdapter.deleteItem(position-1)
        photoAdapter.notifyItemRemoved(position)
        lifecycleScope.launch(Dispatchers.IO){
            db.photoCardDao().delete(favouritePhotoEntity)
        }
    }

    override fun deleteItemFromDB(favouritePhotoEntity: FavouritePhotoEntity) {
        lifecycleScope.launch(Dispatchers.IO){
            db.photoCardDao().delete(favouritePhotoEntity)
        }
    }

}