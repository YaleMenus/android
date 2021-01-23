package com.adisa.diningplus.db.dao

import androidx.room.*
import com.adisa.diningplus.db.entities.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE hall_id = :hallId")
    fun getHall(hallId: String): List<Meal>

    @Insert
    fun insert(meal: Meal)

    @Delete
    fun delete(meal: Meal)

    @Update
    fun update(meal: Meal)

    @Query("DELETE FROM meals WHERE hall_id = :hallId")
    fun clearHall(hallId: String)
}