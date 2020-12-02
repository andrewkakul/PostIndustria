package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * From user_table WHERE name=:login")
    suspend fun userAuthorization(login: String): UserEntity

    @Query("delete from user_table")
    fun deleteAllNotes()
}