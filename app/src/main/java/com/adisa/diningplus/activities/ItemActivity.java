package com.adisa.diningplus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.adisa.diningplus.adapters.AllergenAdapter;
import com.adisa.diningplus.adapters.NutritionAdapter;
import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Nutrition;
import com.adisa.diningplus.fragments.FollowDialogFragment;
import com.adisa.diningplus.adapters.ItemDetailAdapter;
import com.adisa.diningplus.R;
import com.adisa.diningplus.network.API;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ItemActivity extends AppCompatActivity {
    API api;
    SharedPreferences preferences;

    String itemName;
    int itemId;

    ListView itemDetailListView;
    ItemDetailAdapter itemDetailAdapter;

    ListView allergenListView;
    ArrayList<Allergen> allergens = new ArrayList<>();
    ListView nutrientListView;
    ArrayList<Nutrient> nutrients = new ArrayList<>();

    Nutrition nutrition;
    TextView portionSize;
    TextView calories;

    public class Allergen {
        public int image;
        public String name;

        Allergen(int image, String name) {
            this.image = image;
            this.name = name;
        }
    }

    public class Nutrient {
        public String name;
        public String amount;
        public Integer pdv;

        Nutrient(String name, String amount) {
            this.name = name;
            this.amount = amount;
            this.pdv = null;
        }

        Nutrient(String name, String amount, Integer pdv) {
            this.name = name;
            this.amount = amount;
            this.pdv = pdv;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        api = new API(this);

        Intent i = getIntent();
        itemName = i.getStringExtra("name");
        itemId = i.getIntExtra("id", -1);

        allergenListView = (ListView) findViewById(R.id.allergenListView);
        nutrientListView = (ListView) findViewById(R.id.nutrientListView);

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

    private SpannableString mixedWeight(String bold, String regular) {
        SpannableString str = new SpannableString(bold + " " + regular);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    private class ItemTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Item item = null;
            try {
                item = api.getItem(itemId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
            if (item.alcohol) allergens.add(new Allergen(R.drawable.key_alcohol, "Alcohol"));
            if (item.nuts) allergens.add(new Allergen(R.drawable.key_nuts, "Nuts"));
            if (item.shellfish) allergens.add(new Allergen(R.drawable.key_shellfish, "Shellfish"));
            if (item.peanuts) allergens.add(new Allergen(R.drawable.key_peanuts, "Peanuts"));
            if (item.dairy) allergens.add(new Allergen(R.drawable.key_dairy, "Dairy"));
            if (item.egg) allergens.add(new Allergen(R.drawable.key_egg, "Egg"));
            if (item.pork) allergens.add(new Allergen(R.drawable.key_pork, "Pork"));
            if (item.fish) allergens.add(new Allergen(R.drawable.key_fish, "Fish"));
            if (item.soy) allergens.add(new Allergen(R.drawable.key_soy, "Soy"));
            if (item.wheat) allergens.add(new Allergen(R.drawable.key_wheat, "Wheat"));
            if (item.gluten) allergens.add(new Allergen(R.drawable.key_gluten, "Gluten"));
            if (item.coconut) allergens.add(new Allergen(R.drawable.key_coconut, "Coconut"));

            nutrition = null;
            try {
                nutrition = api.getItemNutrition(itemId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
            nutrients.add(new Nutrient("Serving Size", nutrition.portionSize));
            nutrients.add(new Nutrient("Calories", nutrition.calories));
            nutrients.add(new Nutrient("Total Fat", nutrition.totalFat, nutrition.totalFatPDV));
            nutrients.add(new Nutrient("Saturated Fat", nutrition.saturatedFat, nutrition.saturatedFatPDV));
            nutrients.add(new Nutrient("Trans Fat", nutrition.transFat, nutrition.transFatPDV));
            nutrients.add(new Nutrient("Cholesterol", nutrition.cholesterol, nutrition.cholesterolPDV));
            nutrients.add(new Nutrient("Sodium", nutrition.sodium, nutrition.sodiumPDV));
            nutrients.add(new Nutrient("Total Carbohydrate", nutrition.totalCarbohydrate, nutrition.totalCarbohydratePDV));
            nutrients.add(new Nutrient("Dietary Fiber", nutrition.dietaryFiber, nutrition.dietaryFiberPDV));
            nutrients.add(new Nutrient("Total Sugars", nutrition.totalSugars, nutrition.totalSugarsPDV));
            nutrients.add(new Nutrient("Protein", nutrition.protein, nutrition.proteinPDV));
            nutrients.add(new Nutrient("Vitamin D", nutrition.vitaminD, nutrition.vitaminDPDV));
            nutrients.add(new Nutrient("Vitamin A", nutrition.vitaminA, nutrition.vitaminAPDV));
            nutrients.add(new Nutrient("Vitamin C", nutrition.vitaminC, nutrition.vitaminCPDV));
            nutrients.add(new Nutrient("Calcium", nutrition.calcium, nutrition.calciumPDV));
            nutrients.add(new Nutrient("Iron", nutrition.iron, nutrition.ironPDV));
            nutrients.add(new Nutrient("Potassium", nutrition.potassium, nutrition.potassiumPDV));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AllergenAdapter allergenAdapter = new AllergenAdapter(ItemActivity.this, allergens);
            allergenListView.setAdapter(allergenAdapter);
            NutritionAdapter nutritionAdapter = new NutritionAdapter(ItemActivity.this, nutrients);
            nutrientListView.setAdapter(nutritionAdapter);
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
