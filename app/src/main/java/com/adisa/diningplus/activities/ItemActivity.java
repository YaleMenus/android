package com.adisa.diningplus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.adisa.diningplus.db.DatabaseHelper;
import com.adisa.diningplus.db.DatabaseContract;
import com.adisa.diningplus.fragments.FollowDialogFragment;
import com.adisa.diningplus.adapters.ItemDetailAdapter;
import com.adisa.diningplus.R;
import com.adisa.diningplus.network.DiningAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ItemActivity extends AppCompatActivity {
    String itemName;
    int nutritionId;
    DatabaseHelper dbHelper;
    DiningAPI api;
    ListView itemDetailListView;
    ItemDetailAdapter itemDetailAdapter;
    ArrayList<Detail> detailList = new ArrayList<>();
    SharedPreferences preferences;

    public class Detail {
        public int image;
        public String name;

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
        nutritionId = i.getIntExtra("id", -1);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        getSupportActionBar().setTitle(itemName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(getApplicationContext());

        api = new DiningAPI(dbHelper);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("followTutorial", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("followTutorial", false);
            editor.apply();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("follow");
            if (prev == null) {
                DialogFragment followDialog = new FollowDialogFragment();
                followDialog.show(getSupportFragmentManager(), "follow");
            }
        }

        NutritionTask nutritionTask = new NutritionTask();
        nutritionTask.execute();
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

    private class NutritionTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("get", "start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                api.fetchItem(itemId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Cursor nutrition = dbHelper.getItem(itemId);
            while (nutrition.moveToNext()) {
                if (nutrition.getInt(DatabaseContract.Item.ALCOHOL))
                    detailList.add(new Detail(R.drawable.key_alcohol, "Alcohol"));
                if (nutrition.getInt(DatabaseContract.Item.NUTS))
                    detailList.add(new Detail(R.drawable.key_nut, "Nuts"));
                if (nutrition.getInt(DatabaseContract.Item.DAIRY))
                    detailList.add(new Detail(R.drawable.key_dairy, "Dairy"));
                if (nutrition.getInt(DatabaseContract.Item.EGGS))
                    detailList.add(new Detail(R.drawable.key_eggs, "Eggs"));
                if (nutrition.getInt(DatabaseContract.Item.FISH))
                    detailList.add(new Detail(R.drawable.key_fish, "Fish"));
                if (nutrition.getInt(DatabaseContract.Item.GLUTEN))
                    detailList.add(new Detail(R.drawable.key_gluten, "Gluten"));
                if (nutrition.getInt(DatabaseContract.Item.GLUTEN_FREE))
                    detailList.add(new Detail(R.drawable.key_glutenfree, "Gluten Free"));
                if (nutrition.getInt(DatabaseContract.Item.PEANUT))
                    detailList.add(new Detail(R.drawable.key_peanut, "Peanut"));
                if (nutrition.getInt(DatabaseContract.Item.PORK))
                    detailList.add(new Detail(R.drawable.key_pork, "Pork"));
                if (nutrition.getInt(DatabaseContract.Item.SHELLFISH))
                    detailList.add(new Detail(R.drawable.key_shellfish, "Shellfish"));
                if (nutrition.getInt(DatabaseContract.Item.SOY))
                    detailList.add(new Detail(R.drawable.key_soy, "Soy"));
                if (nutrition.getInt(DatabaseContract.Item.VEGAN)) == 1)
                    detailList.add(new Detail(R.drawable.key_vegan, "Vegan"));
                if (nutrition.getInt(DatabaseContract.Item.VEGETARIAN))
                    detailList.add(new Detail(R.drawable.key_vegetarian, "Vegetarian"));
                if (nutrition.getInt(DatabaseContract.Item.WHEAT))
                    detailList.add(new Detail(R.drawable.key_wheat, "Wheat"));
                if (nutrition.getString(DatabaseContract.Nutrition.WARNING)) == null)
                    detailList.add(new Detail(-2, nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.WARNING))));
                if (detailList.size() > 0)
                    detailList.add(0, new Detail(-1, "Traits"));

                detailList.add(new Detail(-1, "Nutrition"));
                detailList.add(new Detail(-3, "Serving Size: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.SERVING_SIZE))));
                detailList.add(new Detail(-3, "Calories: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.CALORIES))));
                detailList.add(new Detail(-3, "Protein: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.PROTEIN))));
                detailList.add(new Detail(-3, "Fat: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.FAT))));
                detailList.add(new Detail(-3, "Saturated Fat: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.SATURATED_FAT))));
                detailList.add(new Detail(-3, "Cholesterol: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.CHOLESTEROL))));
                detailList.add(new Detail(-3, "Carbohydrates: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.CARBOHYDRATES))));
                detailList.add(new Detail(-3, "Sugar: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.SUGAR))));
                detailList.add(new Detail(-3, "Dietary Fiber: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.DIETARY_FIBER))));
                detailList.add(new Detail(-3, "Vitamin C: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.VITAMIN_C))));
                detailList.add(new Detail(-3, "Vitamin A: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.VITAMIN_A))));
                detailList.add(new Detail(-3, "Iron: " + nutrition.getString(nutrition.getColumnIndex(DatabaseContract.Nutrition.IRON))));
            }

            Cursor ingredients = dbHelper.getIngredients(nutritionId);
            detailList.add(new Detail(-1, "Ingredients"));
            while (ingredients.moveToNext()) {
                detailList.add(new Detail(-3, ingredients.getString(ingredients.getColumnIndex(DatabaseContract.Ingredient.NAME))));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            itemDetailAdapter = new ItemDetailAdapter(ItemActivity.this, detailList);
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
