package com.adisa.diningplus.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.adisa.diningplus.db.dao.LocationDao;
import com.adisa.diningplus.db.entities.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}