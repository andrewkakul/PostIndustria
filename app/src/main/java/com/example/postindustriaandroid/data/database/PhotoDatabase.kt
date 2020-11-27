package com.example.postindustriaandroid.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.postindustriaandroid.data.database.dao.PhotoCardDao
import com.example.postindustriaandroid.data.database.dao.UserDao
import com.example.postindustriaandroid.data.database.entity.UserEntitty


@Database(entities = [UserEntitty::class], version = 1)
abstract class PhotoDatabase: RoomDatabase() {
    abstract fun photoCardDao(): PhotoCardDao
    abstract fun userDao(): UserDao

    companion object {
        private var instance: PhotoDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): PhotoDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, PhotoDatabase::class.java,
                    "flickrPhotoCard_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: PhotoDatabase) {
            val noteDao = db.photoCardDao()
            val userDao = db.userDao()
        }
    }
}