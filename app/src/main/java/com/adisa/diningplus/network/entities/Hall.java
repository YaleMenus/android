package com.adisa.diningplus.network.entities;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

public class Hall {
    public String id;
    public String name;
    public String nickname;
    public boolean open;
    public int occupancy;
    public double latitude;
    public double longitude;
    public String address;
    public String phone;

    public double distance;

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

    public void setDistance(Location location) {
        if (location != null) {
            Location loc = new Location("");
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            // Convert m -> km
            distance = location.distanceTo(loc) / 1000;
        }
    }
}