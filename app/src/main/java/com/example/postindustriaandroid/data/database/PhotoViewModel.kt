package com.example.postindustriaandroid.data.database

import androidx.lifecycle.*
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {
    
    val allWords: LiveData<List<FavouritePhotoEntity>> = repository.allWords.asLiveData()

    fun insert(photoEntity: FavouritePhotoEntity) = viewModelScope.launch {
        repository.insert(photoEntity)
    }
}

class PhotoViewModelFactory(private val repository: PhotoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}