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

import com.adisa.diningplus.db.DatabaseClient;
import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Nutrition;
import com.adisa.diningplus.fragments.FollowDialogFragment;
import com.adisa.diningplus.adapters.ItemDetailAdapter;
import com.adisa.diningplus.R;
import com.adisa.diningplus.network.DiningAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ItemActivity extends AppCompatActivity {
    DatabaseClient db;
    DiningAPI api;

    String itemName;
    int itemId;

    ListView itemDetailListView;
    ItemDetailAdapter itemDetailAdapter;
    ArrayList<Allergen> allergens = new ArrayList<>();
    SharedPreferences preferences;

    private class Allergen {
        int image;
        String name;

        Allergen(int image, String name) {
            this.image = image;
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        db = new DatabaseClient(this);
        api = new DiningAPI(db);

        Intent i = getIntent();
        itemName = i.getStringExtra("name");
        itemId = i.getIntExtra("id", -1);

        itemDetailListView = (ListView) findViewById(R.id.itemDetailListView);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        getSupportActionBar().setTitle(itemName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        ItemTask itemTask = new ItemTask();
        itemTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        HashSet<String> current = (HashSet<String>) preferences.getStringSet("followedItems", new HashSet<String>());
        if (current.contains(itemName)) {
            menu.findItem(R.id.action_notify).setIcon(R.drawable.notifications_enabled);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private class ItemTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("get", "start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Item item;
            try {
                item = api.getItem(itemId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            if (item.alcohol) allergens.add(new Allergen(R.drawable.key_alcohol, "Alcohol"));
            if (item.nuts) allergens.add(new Allergen(R.drawable.key_nuts, "Nuts"));
            if (item.shellfish) allergens.add(new Allergen(R.drawable.key_shellfish, "Shellfish"))
            if (item.peanuts) allergens.add(new Allergen(R.drawable.key_peanuts, "Peanuts"));
            if (item.dairy) allergens.add(new Allergen(R.drawable.key_dairy, "Dairy"));
            if (item.egg) allergens.add(new Allergen(R.drawable.key_egg, "Egg"));
            if (item.pork) allergens.add(new Allergen(R.drawable.key_pork, "Pork"));
            if (item.fish) allergens.add(new Allergen(R.drawable.key_fish, "Fish"));
            if (item.soy) allergens.add(new Allergen(R.drawable.key_soy, "Soy"));
            if (item.wheat) allergens.add(new Allergen(R.drawable.key_wheat, "Wheat"));
            if (item.gluten) allergens.add(new Allergen(R.drawable.key_gluten, "Gluten"));
            // TODO: make sure this image renders properly
            if (item.coconut) allergens.add(new Allergen(R.drawable.key_coconut, "Coconut"));

            Nutrition nutrition;
            try {
                nutrition = api.getNutrition(itemId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

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
    */
}
