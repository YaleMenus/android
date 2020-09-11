package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Manager;

import java.util.List;

@Dao
public interface ManagerDao {

    @Query("SELECT * FROM managers")
    List<Manager> getAll();

    @Insert
    void insert(Manager manager);

    @Delete
    void delete(Manager manager);

    @Update
    void update(Manager manager);
}