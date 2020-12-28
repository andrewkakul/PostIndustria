package com.example.postindustriaandroid.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "files_table",
    foreignKeys =
    [ForeignKey(entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)])
data class FilesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val uri: String,
    val user_id: Long
)
