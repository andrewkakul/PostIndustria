package com.example.postindustriaandroid.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "history_table",
    foreignKeys =
        [ForeignKey(entity = UserEntity::class,
                parentColumns = ["id"],
                childColumns = ["user_id"],
                onDelete = ForeignKey.CASCADE)])
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val text: String,
    val user_id: Long
)
