package com.muhaammaad.challenge.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Helper Base Dao Class
 */
interface BaseDao<T> {
    /**
     * Gives the item inserted with Conflicted Strategy Replace
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertId(t: T): Long

    /**
     * inserts item with Conflicted Strategy Replace
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T)

    /**
     * inserts list of  with items with Conflicted Strategy Replace
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: List<T>)

    /**
     * updates item with Conflicted Strategy Replace
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(t: T)

    /**
     * delete the given item
     */
    @Delete
    fun delete(t: T)


}
