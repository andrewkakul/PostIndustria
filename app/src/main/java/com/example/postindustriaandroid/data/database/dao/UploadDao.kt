package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.database.entity.UploadEntity

@Dao
interface UploadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(uploadEntity: UploadEntity)

    @Delete
    fun delete(uploadEntity: UploadEntity)

    @Query("SELECT * From upload_table WHERE user_id=:userId")
    suspend fun getUploadPhoto (userId: Long): List<UploadEntity>

    @Query("delete from upload_table")
    fun deleteAllPhotos()
}