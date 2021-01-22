package com.example.postindustriaandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postindustriaandroid.R
import com.example.postindustriaandroid.data.adapters.DeleteItemListener
import com.example.postindustriaandroid.data.adapters.FavoritePhotoCardAdapter
import com.example.postindustriaandroid.data.adapters.OnFavouriteCardListener
import com.example.postindustriaandroid.data.adapters.SwipeToDeleteFavouriteCallback
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.viewmodel.FavouriteViewModel
import com.example.postindustriaandroid.utils.SharedPrefsManager
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment(),OnFavouriteCardListener, DeleteItemListener {

    private lateinit var db: PhotoRoomDatabase
    private lateinit var recyclerView: RecyclerView
    private val photoAdapter = FavoritePhotoCardAdapter(this)
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = PhotoRoomDatabase.getDatabase(activity?.applicationContext!!)
        recyclerView = favouritePhoto_RV
        initViewModel()
    }

    private fun initViewModel() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = photoAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteFavouriteCallback(photoAdapter, this))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        viewModel.favoriteLiveData.observe(viewLifecycleOwner,{ favouriteList ->
            photoAdapter.setData(favouriteList)
        })
        viewModel.getListOfPhoto(SharedPrefsManager.getUserID(), db)
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