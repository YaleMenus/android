package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Nutrition;

import java.util.List;

@Dao
public interface NutritionDao {

    @Query("SELECT * FROM nutrition")
    List<Nutrition> getAll();

    @Insert
    void insert(Nutrition nutrition);

    @Delete
    void delete(Nutrition nutrition);

    @Update
    void update(Nutrition nutrition);
}