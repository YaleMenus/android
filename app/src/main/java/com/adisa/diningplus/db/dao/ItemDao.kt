package com.adisa.diningplus.db.dao

import androidx.room.*
import com.adisa.diningplus.db.entities.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE meal_id = :mealId")
    fun getMeal(mealId: Int): List<Item?>?

    @Query("SELECT * FROM items WHERE id = :id")
    operator fun get(id: Int): Item?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Item?)

    @Delete
    fun delete(item: Item?)

    @Update
    fun update(item: Item?)

    @Query("DELETE FROM items WHERE meal_id = :mealId")
    fun clearMeal(mealId: Int)
}