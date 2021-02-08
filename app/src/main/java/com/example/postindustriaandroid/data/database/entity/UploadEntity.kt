package com.example.postindustriaandroid.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "upload_table",
    foreignKeys =
    [ForeignKey(entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE)])
data class UploadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val user_id: Long,
    val photoUrl: String,
    val searchText: String
)


