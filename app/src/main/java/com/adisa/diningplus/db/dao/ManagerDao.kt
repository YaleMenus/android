package com.adisa.diningplus.db.dao

import androidx.room.*
import com.adisa.diningplus.db.entities.Manager

@Dao
interface ManagerDao {
    @Query("SELECT * FROM managers")
    fun getAll(): List<Manager?>

    @Insert
    fun insert(manager: Manager?)

    @Delete
    fun delete(manager: Manager?)

    @Update
    fun update(manager: Manager?)
}