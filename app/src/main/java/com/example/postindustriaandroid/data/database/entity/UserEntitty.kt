package com.example.postindustriaandroid.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntitty(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val name: String
)
