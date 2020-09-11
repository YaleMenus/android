package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Location;

import java.util.List;

public class LocationDao {
    @Dao
    public interface TaskDao {

        @Query("SELECT * FROM location")
        List<Location> getAll();

        @Insert
        void insert(Location location);

        @Delete
        void delete(Location location);

        @Update
        void update(Location location);
    }
}
