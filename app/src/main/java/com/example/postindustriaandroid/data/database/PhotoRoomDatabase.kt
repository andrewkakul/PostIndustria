package com.example.postindustriaandroid.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.postindustriaandroid.data.database.dao.FavouritePhotoDao
import com.example.postindustriaandroid.data.database.dao.FilesDao
import com.example.postindustriaandroid.data.database.dao.HistoryDao
import com.example.postindustriaandroid.data.database.dao.UserDao
import com.example.postindustriaandroid.data.database.entity.FavouritePhotoEntity
import com.example.postindustriaandroid.data.database.entity.FilesEntity
import com.example.postindustriaandroid.data.database.entity.HistoryEntity
import com.example.postindustriaandroid.data.database.entity.UserEntity

@Database(version = 2, entities = [UserEntity::class, FavouritePhotoEntity::class, HistoryEntity::class, FilesEntity::class])
abstract class PhotoRoomDatabase: RoomDatabase() {

    abstract fun photoCardDao(): FavouritePhotoDao
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao
    abstract fun filesDao(): FilesDao

    companion object {
        private var INSTANCE: PhotoRoomDatabase? = null

        fun getDatabase(context: Context): PhotoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoRoomDatabase::class.java,
                    "flickr_photo_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE files_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, uri TEXT NOT NULL, user_id INTEGER NOT NULL, FOREIGN KEY(user_id) REFERENCES user_table(id))")
            }
        }
    }
}
