package com.example.postindustriaandroid.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.postindustriaandroid.data.database.dao.*
import com.example.postindustriaandroid.data.database.entity.*

@Database(version = 3, entities = [UserEntity::class
    ,FavouritePhotoEntity::class
    ,HistoryEntity::class
    ,FilesEntity::class
    ,UploadEntity::class])
abstract class PhotoRoomDatabase: RoomDatabase() {

    abstract fun photoCardDao(): FavouritePhotoDao
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao
    abstract fun filesDao(): FilesDao
    abstract fun uploadDao(): UploadDao

    companion object {
        private var INSTANCE: PhotoRoomDatabase? = null

        fun getDatabase(context: Context): PhotoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoRoomDatabase::class.java,
                    "flickr_photo_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE upload_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, photoUrl TEXT NOT NULL, searchText TEXT NOT NULL, user_id INTEGER NOT NULL, FOREIGN KEY(user_id) REFERENCES user_table(id) ON DELETE CASCADE)")
            }
        }
    }
}
