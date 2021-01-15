package com.adisa.diningplus.network;

import android.content.Context;

import com.adisa.diningplus.db.AppDatabase;
import com.adisa.diningplus.db.DatabaseClient;
import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Location;
import com.adisa.diningplus.db.entities.Meal;
import com.adisa.diningplus.db.entities.Nutrition;
import com.adisa.diningplus.utils.DateFormatProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class API {
    private final String API_ROOT = "https://yaledine.herokuapp.com/api/";
    AppDatabase db;

    public API(Context ctx) {
        DatabaseClient dbClient = new DatabaseClient(ctx);
        this.db = dbClient.getDB();
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
        //Log.d("API", buffer.toString());
        return buffer.toString();
    }

    public List<Location> getLocations() throws IOException, JSONException {
        JSONArray locationsRaw = new JSONArray(getJSON("locations"));
        db.locationDao().clear();
        for (int i = 0; i < locationsRaw.length(); i++) {
            JSONObject locationRaw = locationsRaw.getJSONObject(i);
            Location location = new Location();
            location.id = locationRaw.getInt("id");
            location.name = locationRaw.getString("name");
            location.shortname = locationRaw.getString("shortname");
            location.code = locationRaw.getString("code");
            location.open = locationRaw.optBoolean("open", false);
            location.occupancy = locationRaw.optInt("occupancy", 0);
            location.latitude = locationRaw.optDouble("latitude", 0);
            location.longitude = locationRaw.optDouble("longitude", 0);
            location.address = locationRaw.optString("address", null);
            location.phone = locationRaw.optString("phone", null);
            db.locationDao().insert(location);
        }
        return db.locationDao().getAll();
    }

    public List<Meal> getLocationMeals(int locationId, Calendar date) throws IOException, JSONException {
        String query = "date=" + DateFormatProvider.date.format(date.getTime());
        JSONArray mealsRaw = new JSONArray(getJSON("locations/" + locationId + "/meals" + "?" + query));
        db.mealDao().clearLocation(locationId);
        for (int i = 0; i < mealsRaw.length(); i++) {
            JSONObject mealRaw = mealsRaw.getJSONObject(i);
            Meal meal = new Meal();
            meal.id = mealRaw.getInt("id");
            meal.name = mealRaw.getString("name");
            meal.date = mealRaw.getString("date");
            meal.startTime = mealRaw.optString("start_time");
            meal.endTime = mealRaw.optString("end_time");
            meal.locationId = mealRaw.getInt("location_id");
            db.mealDao().insert(meal);
        }
        return db.mealDao().getLocation(locationId);
    }

    public List<Item> getMealItems(int mealId) throws IOException, JSONException {
        List<Item> items = db.itemDao().getMeal(mealId);
        if (!items.isEmpty()) {
            return items;
        }
        ArrayList<Item> fetchedItems = new ArrayList<>();
        JSONArray itemsRaw = new JSONArray(getJSON("meals/" + mealId + "/items"));
        for (int i = 0; i < itemsRaw.length(); i++) {
            JSONObject itemRaw = itemsRaw.getJSONObject(i);
            Item item = new Item();
            item.id = itemRaw.getInt("id");
            item.name = itemRaw.getString("name");
            item.course = itemRaw.getString("course");
            item.ingredients = itemRaw.getString("ingredients");
            item.vegetarian = itemRaw.getBoolean("vegetarian");
            item.vegan = itemRaw.getBoolean("vegan");
            item.alcohol = itemRaw.getBoolean("alcohol");
            item.nuts = itemRaw.getBoolean("nuts");
            item.shellfish = itemRaw.getBoolean("shellfish");
            item.peanuts = itemRaw.getBoolean("peanuts");
            item.dairy = itemRaw.getBoolean("dairy");
            item.egg = itemRaw.getBoolean("egg");
            item.pork = itemRaw.getBoolean("pork");
            item.fish = itemRaw.getBoolean("fish");
            item.soy = itemRaw.getBoolean("soy");
            item.wheat = itemRaw.getBoolean("wheat");
            item.gluten = itemRaw.getBoolean("gluten");
            item.coconut = itemRaw.getBoolean("coconut");
            fetchedItems.add(item);
            // db.itemDao().insert(item);
        }
        return fetchedItems;
        // return db.itemDao().getMeal(mealId);
    }

    public Item getItem(int itemId) throws IOException, JSONException {
        Item item = db.itemDao().get(itemId);
        if (item == null) {
            JSONObject itemRaw = new JSONObject(getJSON("items/" + itemId));
            item = new Item();
            item.id = itemRaw.getInt("id");
            item.name = itemRaw.getString("name");
            item.ingredients = itemRaw.getString("ingredients");
            item.vegetarian = itemRaw.getBoolean("vegetarian");
            item.vegan = itemRaw.getBoolean("vegan");
            item.alcohol = itemRaw.getBoolean("alcohol");
            item.nuts = itemRaw.getBoolean("nuts");
            item.shellfish = itemRaw.getBoolean("shellfish");
            item.peanuts = itemRaw.getBoolean("peanuts");
            item.dairy = itemRaw.getBoolean("dairy");
            item.egg = itemRaw.getBoolean("egg");
            item.pork = itemRaw.getBoolean("pork");
            item.fish = itemRaw.getBoolean("fish");
            item.soy = itemRaw.getBoolean("soy");
            item.wheat = itemRaw.getBoolean("wheat");
            item.gluten = itemRaw.getBoolean("gluten");
            item.coconut = itemRaw.getBoolean("coconut");
            db.itemDao().insert(item);
        }
        return item;
    }

    public Nutrition getItemNutrition(int itemId) throws IOException, JSONException {
        Nutrition nutrition = db.nutritionDao().get(itemId);
        if (nutrition == null) {
            JSONObject nutritionRaw = new JSONObject(getJSON("items/" + itemId + "/nutrition"));
            nutrition = new Nutrition();
            nutrition.portionSize = nutritionRaw.getString("portion_size");

            nutrition.totalFat = nutritionRaw.optString("total_fat");
            nutrition.saturatedFat = nutritionRaw.optString("saturated_fat");
            nutrition.transFat = nutritionRaw.optString("trans_fat");
            nutrition.cholesterol = nutritionRaw.optString("cholesterol");
            nutrition.sodium = nutritionRaw.optString("sodium");
            nutrition.totalCarbohydrate = nutritionRaw.optString("total_carbohydrate");
            nutrition.dietaryFiber = nutritionRaw.optString("dietary_fiber");
            nutrition.totalSugars = nutritionRaw.optString("total_sugars");
            nutrition.protein = nutritionRaw.optString("protein");
            nutrition.vitaminD = nutritionRaw.optString("vitamin_d");
            nutrition.vitaminA = nutritionRaw.optString("vitamin_a");
            nutrition.vitaminC = nutritionRaw.optString("vitamin_c");
            nutrition.calcium = nutritionRaw.optString("calcium");
            nutrition.iron = nutritionRaw.optString("iron");
            nutrition.potassium = nutritionRaw.optString("potassium");

            nutrition.totalFatPDV = nutritionRaw.optInt("total_fat_pdv");
            nutrition.saturatedFatPDV = nutritionRaw.optInt("saturated_fat_pdv");
            nutrition.transFatPDV = nutritionRaw.optInt("trans_fat_pdv");
            nutrition.cholesterolPDV = nutritionRaw.optInt("cholesterol_pdv");
            nutrition.sodiumPDV = nutritionRaw.optInt("sodium_pdv");
            nutrition.totalCarbohydratePDV = nutritionRaw.optInt("total_carbohydrate_pdv");
            nutrition.dietaryFiberPDV = nutritionRaw.optInt("dietary_fiber_pdv");
            nutrition.totalSugarsPDV = nutritionRaw.optInt("total_sugars_pdv");
            nutrition.proteinPDV = nutritionRaw.optInt("protein_pdv");
            nutrition.vitaminDPDV = nutritionRaw.optInt("vitamin_d_pdv");
            nutrition.vitaminAPDV = nutritionRaw.optInt("vitamin_a_pdv");
            nutrition.vitaminCPDV = nutritionRaw.optInt("vitamin_c_pdv");
            nutrition.calciumPDV = nutritionRaw.optInt("calcium_pdv");
            nutrition.ironPDV = nutritionRaw.optInt("iron_pdv");
            nutrition.potassiumPDV = nutritionRaw.optInt("potassium_pdv");

            nutrition.itemId = nutritionRaw.getInt("item_id");
        }
        return nutrition;
    }
}
