package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    val favoriteLiveData = MutableLiveData<ArrayList<Any>>()

    fun getListOfPhoto(userId: Long, db: PhotoRoomDatabase){
        val favoriteData = createList(userId, db)
        favoriteLiveData.postValue(favoriteData)
    }

    private fun createList(userId: Long, db: PhotoRoomDatabase): ArrayList<Any> {
        val favouriteList = ArrayList<Any>()
        var tmp: ArrayList<FavouritePhotoEntity>
        var text = ""
        viewModelScope.launch(Dispatchers.IO) {
                tmp = db.photoCardDao().getListOfFavourite(userId) as ArrayList<FavouritePhotoEntity>
                tmp.forEach {
                    if (it.searchText != text){
                        favouriteList.add(it.searchText)
                    }
                    favouriteList.add(it)
                    text = it.searchText
                }
            }
        return favouriteList
    }
}