package com.example.postindustriaandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.postindustriaandroid.data.database.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("SELECT * From history_table WHERE user_id=:userId")
    suspend fun getHistory (userId: Long): List<HistoryEntity>

    @Query("delete from history_table")
    fun deleteAllHistory()
}