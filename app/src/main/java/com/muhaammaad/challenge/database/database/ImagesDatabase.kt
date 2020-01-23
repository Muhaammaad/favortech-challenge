package com.muhaammaad.challenge.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhaammaad.challenge.database.dao.IUnsplashImages
import com.muhaammaad.challenge.database.entity.UnsplashImages

@Database(
    //region Entities
    entities = [UnsplashImages::class], version = 1, exportSchema = false
    //endregion
)

abstract class ImagesDatabase : RoomDatabase() {

    //region Database Daos

    /**
     * Database entities Daos
     */
    abstract val imagesDao: IUnsplashImages
    //endregion

    companion object {
        val DB_NAME = "images"
    }

}