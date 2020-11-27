package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.PhotoCardEntity

@Dao
interface PhotoCardDao{
    @Insert
    fun insert(photo: PhotoCardEntity)

    @Update
    fun update(photo: PhotoCardEntity)

    @Delete
    fun delete(photo: PhotoCardEntity)

    @Query("delete from photocard_table")
    fun deleteAllNotes()
}
