package com.muhaammaad.challenge.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.muhaammaad.challenge.database.entity.UnsplashImages

@Dao
interface IUnsplashImages : BaseDao<UnsplashImages> {
    /**
     * Returns all the images from unsplashImages table
     */
    @get:Query("SELECT * FROM unsplashImages")
    val allImages: List<UnsplashImages>
}
