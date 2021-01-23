package com.adisa.diningplus.db.dao

import androidx.room.*
import com.adisa.diningplus.db.entities.Hall

@Dao
interface HallDao {
    @Query("SELECT * FROM halls")
    fun getAll(): List<Hall>

    @Insert
    fun insert(hall: Hall?)

    @Delete
    fun delete(hall: Hall?)

    @Update
    fun update(hall: Hall?)

    @Query("DELETE FROM halls")
    fun clear()
}