package com.example.postindustriaandroid.data.database

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.postindustriaandroid.data.database.dao.FavouritePhotoDao
import com.example.postindustriaandroid.data.database.dao.UserDao
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.database.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database( version = 1, entities = [UserEntity::class, FavouritePhotoEntity::class])
abstract class PhotoRoomDatabase: RoomDatabase() {

    abstract fun photoCardDao(): FavouritePhotoDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: PhotoRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PhotoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PhotoRoomDatabase::class.java,
                        "word_database"
                )
                    .addCallback(PhotoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }

    private class PhotoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.photoCardDao())
                }
            }
        }
        fun populateDatabase(favouritePhotoDao: FavouritePhotoDao) {
            favouritePhotoDao.deleteAllPhotos()
        }
    }
}
