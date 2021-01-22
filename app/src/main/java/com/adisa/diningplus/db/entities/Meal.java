package com.adisa.diningplus.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Meal fromJSON(JSONObject raw) throws JSONException {
        Meal meal = new Meal();
        meal.id = raw.getInt("id");
        meal.name = raw.getString("name");
        meal.date = raw.getString("date");
        meal.startTime = raw.optString("start_time");
        meal.endTime = raw.optString("end_time");
        meal.locationId = raw.getInt("location_id");
        return meal;
    }
}
