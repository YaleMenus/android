package com.adisa.diningplus.network;

import android.content.ContentValues;

import com.adisa.diningplus.db.DatabaseClient;
import com.adisa.diningplus.db.entities.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiningAPI {
    private final String API_ROOT = "https://yaledine.herokuapp.com/api/";
    DatabaseClient db;

    public DiningAPI(DatabaseClient db) {
        this.db = db;
    }

    public String getJSON(String endpoint) throws IOException {
        URL url = new URL(API_ROOT + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Read data from connection
        InputStream inputStream = connection.getInputStream();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }

    public void getLocations() throws IOException, JSONException {
        JSONArray locationsRaw = new JSONArray(getJSON("locations"));
        db.getDB().locationDao().clear();
        for (int i = 0; i < locationsRaw.length(); i++) {
            JSONObject locationRaw = locationsRaw.getJSONObject(i);
            String type = locationRaw.getString("type");
            if (type.equals("Residential")) { // TODO: remove if API stops providing non-residential locations
                Location location = new Location();
                location.id = locationRaw.getInt("id");
                location.name = locationRaw.getString("name");
                location.type = type;
                location.isOpen = locationRaw.optBoolean("is_open", false);
                location.capacity = locationRaw.optInt("capacity", 0);
                location.latitude = locationRaw.optDouble("latitude", 0);
                location.longitude = locationRaw.optDouble("longitude", 0);
                location.address = locationRaw.optString("address", null);
                location.phone = locationRaw.optString("phone", null);
                db.getDB().locationDao().insert(location);
            }
        }
    }

    // TODO: rewrite other fetching methods
}
