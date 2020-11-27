package com.example.postindustriaandroid.data.database.dao

import androidx.room.*
import com.example.postindustriaandroid.data.database.entity.UserEntitty

@Dao
interface UserDao {

    @Insert
    fun insert(userEntitty: UserEntitty)

    @Update
    fun update(userEntitty: UserEntitty)

    @Delete
    fun delete(userEntitty: UserEntitty)

    @Query("delete from user_table")
    fun deleteAllNotes()
}