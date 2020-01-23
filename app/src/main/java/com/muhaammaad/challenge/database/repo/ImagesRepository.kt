package com.muhaammaad.challenge.database.repo

import android.content.Context
import androidx.room.Room
import com.muhaammaad.challenge.database.database.ImagesDatabase
import com.muhaammaad.challenge.database.entity.UnsplashImages
import com.muhaammaad.challenge.util.XAppExecutors

class ImagesRepository private constructor() {

    fun insert(image: UnsplashImages) {
        database!!.imagesDao.insert(image)
    }

    fun getimages(): List<UnsplashImages> {
        return database!!.imagesDao.allImages
    }

    companion object {


        private var database: ImagesDatabase? = null
        private var imagesRepository: ImagesRepository? = null

        fun getDatabaseInstance(context: Context): ImagesDatabase {
            if (database == null) {
                database = buildDatabaseInstance(context)
            }
            return database!!
        }

        fun getRepositoryInstance(context: Context): ImagesRepository {
            if (imagesRepository == null) {
                imagesRepository = ImagesRepository()
                getDatabaseInstance(context)
            }
            return imagesRepository!!
        }

        private fun buildDatabaseInstance(context: Context): ImagesDatabase {
            return Room.databaseBuilder(
                context,
                ImagesDatabase::class.java,
                ImagesDatabase.DB_NAME
            )
                .setQueryExecutor(XAppExecutors.instance.diskIO())
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
