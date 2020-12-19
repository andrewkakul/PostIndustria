package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.FilesEntity

@Dao
interface FilesDao {
    @Insert
    suspend fun insert(filesEntity: FilesEntity)

    @Delete
    suspend fun delete(filesEntity: FilesEntity)

    @Query("SELECT * From files_table WHERE user_id=:userId")
    suspend fun getUserFiles (userId: Long): List<FilesEntity>
}