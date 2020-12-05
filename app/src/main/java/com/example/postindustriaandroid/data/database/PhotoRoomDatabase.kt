package com.example.postindustriaandroid.data.database

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.postindustriaandroid.data.database.dao.FavouritePhotoDao
import com.example.postindustriaandroid.data.database.dao.HistoryDao
import com.example.postindustriaandroid.data.database.dao.UserDao
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.database.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database( version = 1, entities = [UserEntity::class, FavouritePhotoEntity::class, HistoryEntity::class])
abstract class PhotoRoomDatabase: RoomDatabase() {

    abstract fun photoCardDao(): FavouritePhotoDao
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao

    companion object {
        private var INSTANCE: PhotoRoomDatabase? = null

        fun getDatabase(context: Context): PhotoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoRoomDatabase::class.java,
                    "flickr_photo_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
