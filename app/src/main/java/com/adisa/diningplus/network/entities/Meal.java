package com.adisa.diningplus.network.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Meal {
    public int id;
    public String name;
    public String date;
    public String startTime;
    public String endTime;
    public String hallId;

    public static Meal fromJSON(JSONObject raw) throws JSONException {
        Meal meal = new Meal();
        meal.id = raw.getInt("id");
        meal.name = raw.getString("name");
        meal.date = raw.getString("date");
        meal.startTime = raw.optString("start_time");
        meal.endTime = raw.optString("end_time");
        meal.hallId = raw.getString("hall_id");
        return meal;
    }
}
