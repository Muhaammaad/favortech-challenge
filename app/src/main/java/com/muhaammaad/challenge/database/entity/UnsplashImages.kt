package com.muhaammaad.challenge.database.entity

import androidx.room.*

import java.util.ArrayList


@Entity(
    tableName = "unsplashImages",
    indices = arrayOf(Index(value = ["photoId"], unique = true))
)
class UnsplashImages(photoID: String = "", bytesString: String = "", name: String = "") {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
    var photoId: String? = photoID
    var thumbEncodedBytes: String? = bytesString
    var userName: String? = name

}