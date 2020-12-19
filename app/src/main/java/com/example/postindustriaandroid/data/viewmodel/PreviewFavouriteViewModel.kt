package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PreviewFavouriteViewModel(application: Application) : AndroidViewModel(application) {

    val isFavorite = MutableLiveData<Boolean>()

    fun loadData(photoUrl: String, userId: Long, db: PhotoRoomDatabase){
        viewModelScope.launch(Dispatchers.IO){
            isFavorite.postValue(
                db.photoCardDao().isExists(photoUrl,userId)
            )
        }
    }
    
    fun saveData(photoUrl: String, searchText: String, db: PhotoRoomDatabase, userId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite.value == true){
                db.photoCardDao().insert(FavouritePhotoEntity(0, userId, photoUrl, searchText))
            }else {
                val favouritePhotoEntity =
                    db.photoCardDao().getFavourite(photoUrl = photoUrl, userID = userId)
                favouritePhotoEntity.let {
                    db.photoCardDao().delete(favouritePhotoEntity)}
            }
        }
    }
}