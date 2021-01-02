package com.adisa.diningplus.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "locations")
public class Location implements Serializable {
    @PrimaryKey
    public int id;
    public String name;
    public String shortname;
    public String code;
    @ColumnInfo(name = "is_open")
    public boolean isOpen;
    public int capacity;
    public double latitude;
    public double longitude;
    public String address;
    public String phone;
}