package com.adisa.diningplus.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "locations")
public class Location implements Serializable {
    @PrimaryKey
    public String id;
    public String name;
    public String shortname;
    public boolean open;
    public int occupancy;
    public double latitude;
    public double longitude;
    public String address;
    public String phone;

    public static Location fromJSON(JSONObject raw) throws JSONException {
        Location location = new Location();
        location.id = raw.getString("id");
        location.name = raw.getString("name");
        location.shortname = raw.getString("shortname");
        location.open = raw.optBoolean("open");
        location.occupancy = raw.optInt("occupancy");
        location.latitude = raw.optDouble("latitude");
        location.longitude = raw.optDouble("longitude");
        location.address = raw.optString("address");
        location.phone = raw.optString("phone");
        return location;
    }
}