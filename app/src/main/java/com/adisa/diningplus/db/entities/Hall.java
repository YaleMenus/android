package com.adisa.diningplus.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "halls")
public class Hall implements Serializable {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String nickname;
    public boolean open;
    public int occupancy;
    public double latitude;
    public double longitude;
    public String address;
    public String phone;

    public static Hall fromJSON(JSONObject raw) throws JSONException {
        Hall hall = new Hall();
        hall.id = raw.getString("id");
        hall.name = raw.getString("name");
        hall.nickname = raw.getString("nickname");
        hall.open = raw.optBoolean("open");
        hall.occupancy = raw.optInt("occupancy");
        hall.latitude = raw.optDouble("latitude");
        hall.longitude = raw.optDouble("longitude");
        hall.address = raw.optString("address");
        hall.phone = raw.optString("phone");
        return hall;
    }
}