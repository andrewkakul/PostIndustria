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

    val favoriteLiveData = MutableLiveData<ArrayList<Any>>()

    fun getListOfPhoto(userId: Long, db: PhotoRoomDatabase){
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteData = createList(userId, db)
            val tmp = createListAny(favoriteData)
            favoriteLiveData.postValue(tmp)
        }
    }

    private fun createList(userId: Long, db: PhotoRoomDatabase): ArrayList<FavouritePhotoCard> {
        val favouriteList = ArrayList<FavouritePhotoCard>()
        viewModelScope.launch(Dispatchers.IO) {
            val listOfText: List<String> = db.photoCardDao().getTextList(userId)
            listOfText.forEach {
                val photoList: ArrayList<FavouritePhotoEntity> =
                    db.photoCardDao().getListByText(it) as ArrayList<FavouritePhotoEntity>
                val favouriteItem = FavouritePhotoCard(photoList, it)
                favouriteList.add(favouriteItem)
            }
        }
        return favouriteList
    }

    private fun createListAny(favouriteData: ArrayList<FavouritePhotoCard>): ArrayList<Any>{
        val favouriteList = ArrayList<Any>()
        favouriteData.forEach {
            favouriteList.add(it)
            it.photos.forEach {
                favouriteList.add(it)
            }
        }
        return favouriteList
    }
}