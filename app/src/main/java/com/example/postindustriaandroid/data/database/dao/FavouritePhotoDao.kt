package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePhotoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favouritePhoto: FavouritePhotoEntity)

    @Delete
    fun delete(favouritePhoto: FavouritePhotoEntity)

    @Query("SELECT EXISTS(SELECT * FROM favouritePhoto_table WHERE photoUrl=:photoUrl AND user_id=:userID)")
    fun isExists(photoUrl: String, userID: Long): Boolean

    @Query("SELECT * FROM favouritePhoto_table WHERE photoUrl=:photoUrl AND user_id=:userID")
    suspend fun getFavourite(photoUrl: String, userID: Long): FavouritePhotoEntity

    @Query("SELECT * FROM favouritePhoto_table WHERE user_id=:userID")
    suspend fun getListOfFavourite(userID: Long): List<FavouritePhotoEntity>

    @Query("SELECT searchText FROM favouritePhoto_table WHERE user_id=:userID GROUP BY searchText")
    fun getTextList(userID: Long): List<String>

    @Query("SELECT * FROM favouritePhoto_table WHERE searchText =:searchText")
    suspend fun getListByText(searchText: String): List<FavouritePhotoEntity>

    @Query("delete from favouritePhoto_table")
    fun deleteAllPhotos()
}
