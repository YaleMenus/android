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
        for (int i = 0; i < locationsRaw.length(); i++) {
            JSONObject locationRaw = locationsRaw.getJSONObject(i);
            Location location = new Location();
            location.id = locationRaw.getInt("id");
            location.name = locationRaw.getString("name");
            location.type = locationRaw.getString("type");
            location.isOpen = locationRaw.getBoolean("is_open");
            location.capacity = locationRaw.getInt("capacity");
            location.latitude = locationRaw.getDouble("latitude");
            location.longitude = locationRaw.getDouble("longitude");
            location.address = locationRaw.getString("address");
            location.phone = locationRaw.getString("phone");
            db.getDB().locationDao().insert(location);
        }
    }

    // TODO: rewrite other fetching methods
}
