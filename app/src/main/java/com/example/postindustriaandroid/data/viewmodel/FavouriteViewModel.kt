package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.model.FavouritePhotoCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    val favoriteLiveData = MutableLiveData<List<FavouritePhotoCard>>()

    fun getListOfPhoto(userId: Long, db: PhotoRoomDatabase){
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteData = db.photoCardDao().getListOfFavourite(userId) as ArrayList<FavouritePhotoEntity>
            val tmp = createList(favouriteData)
            favoriteLiveData.postValue(tmp)
        }
    }

    fun createList(favouriteData: ArrayList<FavouritePhotoEntity>): ArrayList<FavouritePhotoCard>{
        val newList = ArrayList<FavouritePhotoCard>()
        var lastFavouritePhotoCard: FavouritePhotoCard? = null

        for(item in favouriteData){
            val searchText = item.searchText
            val valueObject = if (lastFavouritePhotoCard == null || lastFavouritePhotoCard.searchText != searchText){
                val tmp = FavouritePhotoCard(ArrayList(), searchText)
                newList.add(tmp)
                tmp
            }else{
                lastFavouritePhotoCard
            }
            lastFavouritePhotoCard = valueObject
            valueObject.photos.add(item)
        }
        return newList
    }
}