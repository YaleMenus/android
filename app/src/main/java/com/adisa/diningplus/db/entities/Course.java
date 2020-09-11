package com.adisa.diningplus.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "courses")
public class Course implements Serializable {
    @PrimaryKey
    public int id;
    public String name;
    public String email;
    @ColumnInfo(name = "location_id")
    public int locationId;
}