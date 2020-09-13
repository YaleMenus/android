package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Meal;

import java.util.List;

@Dao
public interface MealDao {

    @Query("SELECT * FROM meals")
    List<Meal> getAll();

    @Insert
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);

    @Query("DELETE FROM meals WHERE location_id = :locationId")
    void clearLocation(int locationId);
}