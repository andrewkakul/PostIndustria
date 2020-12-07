package com.example.postindustriaandroid.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.postindustriaandroid.data.database.PhotoRoomDatabase
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    val listOfHistory = MutableLiveData<ArrayList<HistoryEntity>>()

    fun getListHistory(userId: Long, db: PhotoRoomDatabase){
        viewModelScope.launch(Dispatchers.IO){
            listOfHistory.postValue(db.historyDao().getHistory(userId) as ArrayList<HistoryEntity>)
        }
    }
}