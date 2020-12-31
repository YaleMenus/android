package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Query("SELECT * FROM items WHERE meal_id = :mealId")
    List<Item> getMeal(int mealId);

    @Query("SELECT * FROM items WHERE id = :id")
    Item get(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Delete
    void delete(Item item);

    @Update
    void update(Item item);

    @Query("DELETE FROM items WHERE meal_id = :mealId")
    void clearMeal(int mealId);
}