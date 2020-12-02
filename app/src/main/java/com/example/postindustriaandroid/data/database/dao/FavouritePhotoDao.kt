package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePhotoDao {

    @Insert
    fun insert(favouritePhoto: FavouritePhotoEntity)

    @Delete
    fun delete(favouritePhoto: FavouritePhotoEntity)

    @Query("SELECT * FROM favouritePhoto_table WHERE photoUrl=:photoUrl AND user_id=:userID")
    suspend fun checkIsFavourite(photoUrl: String, userID: Long): FavouritePhotoEntity

    @Query("SELECT * FROM favouritePhoto_table ORDER BY searchText ASC")
    fun getAlphabetizedPhoto(): Flow<List<FavouritePhotoEntity>>

    @Query("delete from favouritePhoto_table")
    fun deleteAllPhotos()
}
