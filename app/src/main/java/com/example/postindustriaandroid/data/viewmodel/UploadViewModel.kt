package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.database.entity.UploadEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadViewModel(application: Application) : AndroidViewModel(application) {

    val uploadPhoto = MutableLiveData<ArrayList<UploadEntity>>()

    fun getListUpload(userId: Long, db: PhotoRoomDatabase){
        viewModelScope.launch(Dispatchers.IO){
            uploadPhoto.postValue(db.uploadDao().getUploadPhoto(userId) as ArrayList<UploadEntity>)
        }
    }
}