package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.FilesEntity
import kotlinx.coroutines.launch

class FilesViewModel(application: Application) : AndroidViewModel(application) {

    val filesLiveData = MutableLiveData<ArrayList<FilesEntity>>()

    fun getListOfFiles(db: PhotoRoomDatabase, userId: Long){
        viewModelScope.launch {
            filesLiveData.postValue(db.filesDao().getUserFiles(userId) as ArrayList<FilesEntity>)
        }
    }
}