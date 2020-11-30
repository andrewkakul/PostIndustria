package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("delete from user_table")
    fun deleteAllNotes()
}