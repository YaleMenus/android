package com.adisa.diningplus.db.dao

import androidx.room.*
import com.adisa.diningplus.db.entities.Nutrition

@Dao
interface NutritionDao {
    @Query("SELECT * FROM nutrition WHERE item_id = :itemId")
    fun get(itemId: Int): Nutrition?

    @Insert
    fun insert(nutrition: Nutrition)

    @Delete
    fun delete(nutrition: Nutrition)

    @Update
    fun update(nutrition: Nutrition)
}