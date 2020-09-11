package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);

    @Update
    void update(Item item);
}