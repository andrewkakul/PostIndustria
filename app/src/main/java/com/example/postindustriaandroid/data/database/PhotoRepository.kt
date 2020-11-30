package com.example.postindustriaandroid.data.database

import androidx.annotation.WorkerThread
import com.example.postindustriaandroid.data.database.dao.FavouritePhotoDao
import com.example.postindustriaandroid.data.database.dao.UserDao
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.coroutines.flow.Flow

class PhotoRepository(private val favouritePhotoDao: FavouritePhotoDao) {
    val allWords: Flow<List<FavouritePhotoEntity>> = favouritePhotoDao.getAlphabetizedPhoto()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(favouritePhotoEntity: FavouritePhotoEntity) {
        favouritePhotoDao.insert(favouritePhotoEntity)
    }
}
