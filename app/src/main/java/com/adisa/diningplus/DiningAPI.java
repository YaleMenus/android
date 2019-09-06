package com.adisa.diningplus;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DiningAPI {
    DiningDbHelper dbHelper;

    public DiningAPI(DiningDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public static JSONArray getJSON(String endpoint, String param) throws IOException, JSONException {
        URL url = new URL("https://www.yaledining.org/fasttrack/" + endpoint + ".cfm" + "?version=3&" + param);
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
        return new JSONObject(buffer.toString()).getJSONArray("DATA");
    }

    public void fetchHalls() throws IOException, JSONException {
        Log.d("URI", "making request");
        JSONArray resultData = getJSON("locations", "");
        for (int i = 0; i < resultData.length(); i++) {
            JSONArray array = resultData.getJSONArray(i);
            if (array.getString(3).equals("Residential")) {
                ContentValues values = new ContentValues();
                values.put(DiningContract.DiningHall._ID, array.getInt(0));
                values.put(DiningContract.DiningHall.NAME, array.getString(2));
                values.put(DiningContract.DiningHall.TYPE, array.getString(3));
                values.put(DiningContract.DiningHall.CAPACITY, array.getInt(4));

                String[] coords = array.getString(5).split(",");
                values.put(DiningContract.DiningHall.LATITUDE, coords[0]);
                values.put(DiningContract.DiningHall.LONGITUDE, coords[1]);

                values.put(DiningContract.DiningHall.IS_CLOSED, array.getInt(6));
                values.put(DiningContract.DiningHall.ADDRESS, array.getString(7));
                values.put(DiningContract.DiningHall.PHONE, array.getString(8));
                values.put(DiningContract.DiningHall.MANAGER1_NAME, array.getString(9));
                values.put(DiningContract.DiningHall.MANAGER1_EMAIL, array.getString(10));
                values.put(DiningContract.DiningHall.MANAGER2_NAME, array.getString(11));
                values.put(DiningContract.DiningHall.MANAGER2_EMAIL, array.getString(12));
                values.put(DiningContract.DiningHall.MANAGER3_NAME, array.getString(13));
                values.put(DiningContract.DiningHall.MANAGER3_EMAIL, array.getString(14));
                values.put(DiningContract.DiningHall.MANAGER4_NAME, array.getString(15));
                values.put(DiningContract.DiningHall.MANAGER4_EMAIL, array.getString(16));
                if (!dbHelper.itemInDb(DiningContract.DiningHall.TABLE_NAME,
                                       DiningContract.DiningHall._ID,
                                       values.getAsInteger(DiningContract.MenuItem._ID).toString())) {
                    values.put(DiningContract.DiningHall.LAST_UPDATED, "");
                    dbHelper.insertHall(values);
                } else {
                    dbHelper.updateHall(values);
                }
            //}
        }
    }

    public void fetchMenu(int hallId) throws JSONException, ParseException, IOException {
        JSONArray menuData = getJSON("menus", "location=" + hallId);
        for (int j = 0; j < menuData.length(); j++) {
            JSONArray resArray = menuData.getJSONArray(j);

            ContentValues menuItem = new ContentValues();
            menuItem.put(DiningContract.MenuItem.DINING_HALL, resArray.getInt(0));
            menuItem.put(DiningContract.MenuItem.MENU_NAME, resArray.getString(3));
            menuItem.put(DiningContract.MenuItem.MENU_CODE, resArray.getInt(4));
            String dateString = resArray.getString(5);
            Date date = DateFormatProvider.full.parse(dateString);
            menuItem.put(DiningContract.MenuItem.DATE, DateFormatProvider.date.format(date));
            menuItem.put(DiningContract.MenuItem._ID, resArray.getInt(6));
            menuItem.put(DiningContract.MenuItem.NUTRITION_ID, resArray.getInt(9));
            menuItem.put(DiningContract.MenuItem.NAME, resArray.getString(10));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Calendar time = Calendar.getInstance();
            time.setTime(DateFormatProvider.hour.parse(resArray.getString(12)));
            cal.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            menuItem.put(DiningContract.MenuItem.START_TIME, DateFormatProvider.time.format(cal.getTime()));
            time.setTime(DateFormatProvider.hour.parse(resArray.getString(13)));
            cal.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            menuItem.put(DiningContract.MenuItem.END_TIME, DateFormatProvider.time.format(cal.getTime()));
            if (!dbHelper.itemInDb(DiningContract.MenuItem.TABLE_NAME, DiningContract.MenuItem._ID, menuItem.getAsInteger(DiningContract.MenuItem._ID).toString())) {
                dbHelper.insertMenuItem(menuItem);
            } else {
                dbHelper.updateMenuItem(menuItem);
            }
        }
    }

    public void fetchItem(int nutId) throws IOException, JSONException {
        if (!dbHelper.itemInDb(DiningContract.NutritionItem.TABLE_NAME, DiningContract.NutritionItem._ID, nutId + "")) {
            JSONArray nutData = getJSON("menuitem-nutrition", "MENUITEMID=" + nutId);
            ContentValues nutItem = new ContentValues();
            for (int k = 0; k < nutData.length(); k++) {
                JSONArray nutarray = nutData.getJSONArray(k);
                nutItem.put(DiningContract.NutritionItem._ID, nutarray.getInt(0));
                nutItem.put(DiningContract.NutritionItem.NAME, nutarray.getString(1));
                nutItem.put(DiningContract.NutritionItem.SERVING_SIZE, nutarray.getString(2));
                nutItem.put(DiningContract.NutritionItem.CALORIES, nutarray.getString(3));
                nutItem.put(DiningContract.NutritionItem.PROTEIN, nutarray.getString(4));
                nutItem.put(DiningContract.NutritionItem.FAT, nutarray.getString(5));
                nutItem.put(DiningContract.NutritionItem.SATURATED_FAT, nutarray.getString(6));
                nutItem.put(DiningContract.NutritionItem.CHOLESTEROL, nutarray.getString(7));
                nutItem.put(DiningContract.NutritionItem.CARBOHYDRATES, nutarray.getString(8));
                nutItem.put(DiningContract.NutritionItem.SUGAR, nutarray.getString(9));
                nutItem.put(DiningContract.NutritionItem.DIETARY_FIBER, nutarray.getString(10));
                nutItem.put(DiningContract.NutritionItem.VITAMIN_C, nutarray.getString(11));
                nutItem.put(DiningContract.NutritionItem.VITAMIN_A, nutarray.getString(12));
                nutItem.put(DiningContract.NutritionItem.IRON, nutarray.getString(13));
            }
            JSONArray traitData = getJSON("menuitem-codes", "MENUITEMID=" + nutId);
            for (int l = 0; l < traitData.length(); l++) {
                JSONArray traitarray = traitData.getJSONArray(l);
                nutItem.put(DiningContract.NutritionItem.ALCOHOL, traitarray.getInt(2));
                nutItem.put(DiningContract.NutritionItem.NUTS, traitarray.getInt(3));
                nutItem.put(DiningContract.NutritionItem.SHELLFISH, traitarray.getInt(4));
                nutItem.put(DiningContract.NutritionItem.PEANUT, traitarray.getInt(5));
                nutItem.put(DiningContract.NutritionItem.DAIRY, traitarray.getInt(6));
                nutItem.put(DiningContract.NutritionItem.EGGS, traitarray.getInt(7));
                nutItem.put(DiningContract.NutritionItem.VEGAN, traitarray.getInt(8));
                nutItem.put(DiningContract.NutritionItem.PORK, traitarray.getInt(9));
                nutItem.put(DiningContract.NutritionItem.FISH, traitarray.getInt(10));
                nutItem.put(DiningContract.NutritionItem.SOY, traitarray.getInt(11));
                nutItem.put(DiningContract.NutritionItem.WHEAT, traitarray.getInt(12));
                nutItem.put(DiningContract.NutritionItem.GLUTEN, traitarray.getInt(13));
                nutItem.put(DiningContract.NutritionItem.VEGETARIAN, traitarray.getInt(14));
                nutItem.put(DiningContract.NutritionItem.GLUTEN_FREE, traitarray.getInt(15));
                nutItem.put(DiningContract.NutritionItem.WARNING, traitarray.getString(16));
            }
            dbHelper.insertNutritionItem(nutItem);
            JSONArray ingredientData = getJSON("https://www.yaledining.org/fasttrack/menuitem-ingredients.cfm?MENUITEMID="
                    + nutId + "&version=3");
            for (int l = 0; l < ingredientData.length(); l++) {
                JSONArray ingredarray = ingredientData.getJSONArray(l);
                ContentValues ingredients = new ContentValues();
                ingredients.put(DiningContract.Ingredient.NUTRITION_ID, ingredarray.getInt(0));
                ingredients.put(DiningContract.NutritionItem.NAME, ingredarray.getString(1));
                dbHelper.insertIngredient(ingredients);
            }
        }
    }
}
