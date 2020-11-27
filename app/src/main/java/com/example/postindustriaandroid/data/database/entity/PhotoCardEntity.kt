package com.example.postindustriaandroid.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "photocard_table",
        foreignKeys =
                [ForeignKey(entity = UserEntitty::class,
                        parentColumns = ["userId"],
                        childColumns = ["ownerId"])])
data class PhotoCardEntity(
        @PrimaryKey(autoGenerate = true) val  photoCardId: Int,
        val ownerId: Int,
        val photoUrl: String,
        val searchText: String
)
