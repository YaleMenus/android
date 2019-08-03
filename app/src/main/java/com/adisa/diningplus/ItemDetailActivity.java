package com.adisa.diningplus;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ItemDetailActivity extends AppCompatActivity {
    String itemName;
    int nutId;
    DiningDbHelper dbHelper;
    ListView itemDetailListView;
    ItemDetailAdapter itemDetailAdapter;
    ArrayList<Detail> detailList = new ArrayList<>();
    SharedPreferences preferences;

    class Detail {
        int image;
        String name;

        Detail(int image, String name) {
            this.image = image;
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        itemDetailListView = (ListView) findViewById(R.id.itemDetailListView);
        Intent i = getIntent();
        itemName = i.getStringExtra("name");
        nutId = i.getIntExtra("id", -1);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        getSupportActionBar().setTitle(itemName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DiningDbHelper(getApplicationContext());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("followTut", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("followTut", false);
            editor.apply();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("follow");
            if (prev == null) {
                DialogFragment followDialog = new FollowDialogFragment();
                followDialog.show(getSupportFragmentManager(), "follow");
            }
        }

        NutTask nutTask = new NutTask();
        nutTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_detail, menu);
        HashSet<String> current = (HashSet<String>) preferences.getStringSet("followedItems", new HashSet<String>());
        if (current.contains(itemName)) {
            menu.findItem(R.id.action_notify).setIcon(R.drawable.notifications_enabled);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public static void setNutItem(DiningDbHelper dbHelper, int nutId) throws IOException, JSONException {
        if (!dbHelper.itemInDb(DiningContract.NutritionItem.TABLE_NAME, DiningContract.NutritionItem._ID, nutId + "")) {
            JSONArray nutData = MainActivity.getJSON("https://www.yaledining.org/fasttrack/menuitem-nutrition.cfm?MENUITEMID="
                    + nutId + "&version=3");
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
            JSONArray traitData = MainActivity.getJSON("https://www.yaledining.org/fasttrack/menuitem-codes.cfm?MENUITEMID="
                    + nutId + "&version=3");
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
            JSONArray ingredientData = MainActivity.getJSON("https://www.yaledining.org/fasttrack/menuitem-ingredients.cfm?MENUITEMID="
                    + nutId + "&version=3");
            for (int l = 0; l < ingredientData.length(); l++) {
                JSONArray ingredarray = ingredientData.getJSONArray(l);
                ContentValues ingred = new ContentValues();
                ingred.put(DiningContract.Ingredient.NUTRITION_ID, ingredarray.getInt(0));
                ingred.put(DiningContract.NutritionItem.NAME, ingredarray.getString(1));
                dbHelper.insertIngredient(ingred);
            }
        }

    }

    private class NutTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("get", "start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                setNutItem(dbHelper, nutId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Cursor itemDetails = dbHelper.getNutritionItem(nutId);
            while (itemDetails.moveToNext()) {
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.ALCOHOL)) == 1)
                    detailList.add(new Detail(R.drawable.key_alcohol, "Alcohol"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.NUTS)) == 1)
                    detailList.add(new Detail(R.drawable.key_nut, "Nuts"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.DAIRY)) == 1)
                    detailList.add(new Detail(R.drawable.key_dairy, "Dairy"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.EGGS)) == 1)
                    detailList.add(new Detail(R.drawable.key_eggs, "Eggs"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.FISH)) == 1)
                    detailList.add(new Detail(R.drawable.key_fish, "Fish"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.GLUTEN)) == 1)
                    detailList.add(new Detail(R.drawable.key_gluten, "Gluten"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.GLUTEN_FREE)) == 1)
                    detailList.add(new Detail(R.drawable.key_glutenfree, "Gluten Free"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.PEANUT)) == 1)
                    detailList.add(new Detail(R.drawable.key_peanut, "Peanut"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.PORK)) == 1)
                    detailList.add(new Detail(R.drawable.key_pork, "Pork"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.SHELLFISH)) == 1)
                    detailList.add(new Detail(R.drawable.key_shellfish, "Shellfish"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.SOY)) == 1)
                    detailList.add(new Detail(R.drawable.key_soy, "Soy"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.VEGAN)) == 1)
                    detailList.add(new Detail(R.drawable.key_vegan, "Vegan"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.VEGETARIAN)) == 1)
                    detailList.add(new Detail(R.drawable.key_vegetarian, "Vegetarian"));
                if (itemDetails.getInt(itemDetails.getColumnIndex(DiningContract.NutritionItem.WHEAT)) == 1)
                    detailList.add(new Detail(R.drawable.key_wheat, "Wheat"));
                if (itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.WARNING)) == null)
                    detailList.add(new Detail(-2, itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.WARNING))));
                if (detailList.size() > 0)
                    detailList.add(0, new Detail(-1, "Traits"));

                detailList.add(new Detail(-1, "Nutrition"));
                detailList.add(new Detail(-3, "Serving Size: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.SERVING_SIZE))));
                detailList.add(new Detail(-3, "Calories: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.CALORIES))));
                detailList.add(new Detail(-3, "Protein: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.PROTEIN))));
                detailList.add(new Detail(-3, "Fat: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.FAT))));
                detailList.add(new Detail(-3, "Saturated Fat: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.SATURATED_FAT))));
                detailList.add(new Detail(-3, "Cholesterol: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.CHOLESTEROL))));
                detailList.add(new Detail(-3, "Carbohydrates: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.CARBOHYDRATES))));
                detailList.add(new Detail(-3, "Sugar: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.SUGAR))));
                detailList.add(new Detail(-3, "Dietary Fiber: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.DIETARY_FIBER))));
                detailList.add(new Detail(-3, "Vitamin C: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.VITAMIN_C))));
                detailList.add(new Detail(-3, "Vitamin A: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.VITAMIN_A))));
                detailList.add(new Detail(-3, "Iron: " + itemDetails.getString(itemDetails.getColumnIndex(DiningContract.NutritionItem.IRON))));
            }

            Cursor ingredients = dbHelper.getIngredients(nutId);
            detailList.add(new Detail(-1, "Ingredients"));
            while (ingredients.moveToNext()) {
                detailList.add(new Detail(-3, ingredients.getString(ingredients.getColumnIndex(DiningContract.Ingredient.NAME))));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            itemDetailAdapter = new ItemDetailAdapter(ItemDetailActivity.this, detailList);
            itemDetailListView.setAdapter(itemDetailAdapter);
            Log.e("get", "done");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_notify:
                SharedPreferences.Editor editor = preferences.edit();
                HashSet<String> currentSet = (HashSet<String>) preferences.getStringSet("followedItems", new HashSet<String>());
                HashSet<String> newSet = new HashSet<String>();
                newSet.addAll(currentSet);
                if (!currentSet.contains(itemName)) {
                    item.setIcon(R.drawable.notifications_enabled);
                    newSet.add(itemName);
                } else {
                    item.setIcon(R.drawable.notifications_disabled);
                    newSet.remove(itemName);
                }
                editor.putStringSet("followedItems", newSet);
                editor.apply();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
