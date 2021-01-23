package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Hall;

import java.util.List;

@Dao
public interface HallDao {

    @Query("SELECT * FROM halls")
    List<Hall> getAll();

    @Insert
    void insert(Hall hall);

    @Delete
    void delete(Hall hall);

    @Update
    void update(Hall hall);

    @Query("DELETE FROM halls")
    void clear();
}