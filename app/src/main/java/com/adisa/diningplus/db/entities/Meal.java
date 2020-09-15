package com.adisa.diningplus.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "meals")
public class Meal implements Serializable {
    @PrimaryKey
    public int id;
    public String name;
    public String date;
    @ColumnInfo(name = "start_time")
    public String startTime;
    @ColumnInfo(name = "end_time")
    public String endTime;
    @ColumnInfo(name = "location_id")
    public int locationId;
}
