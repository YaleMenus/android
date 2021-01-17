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

    Nutrition nutrition;
    TextView portionSize;
    TextView calories;

    TextView totalFat;
    TextView saturatedFat;
    TextView transFat;
    TextView cholesterol;
    TextView sodium;
    TextView totalCarbohydrate;
    TextView dietaryFiber;
    TextView totalSugars;
    TextView protein;
    TextView vitaminD;
    TextView vitaminA;
    TextView vitaminC;
    TextView calcium;
    TextView iron;
    TextView potassium;

    TextView portionSizePDV;
    TextView caloriesPDV;
    TextView totalFatPDV;
    TextView saturatedFatPDV;
    TextView transFatPDV;
    TextView cholesterolPDV;
    TextView sodiumPDV;
    TextView totalCarbohydratePDV;
    TextView dietaryFiberPDV;
    TextView totalSugarsPDV;
    TextView proteinPDV;
    TextView vitaminDPDV;
    TextView vitaminAPDV;
    TextView vitaminCPDV;
    TextView calciumPDV;
    TextView ironPDV;
    TextView potassiumPDV;

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

        portionSize = findViewById(R.id.portionSize);

        totalFat = findViewById(R.id.totalFat);
        saturatedFat = findViewById(R.id.saturatedFat);
        transFat = findViewById(R.id.transFat);
        cholesterol = findViewById(R.id.cholesterol);
        sodium = findViewById(R.id.sodium);
        totalCarbohydrate = findViewById(R.id.totalCarbohydrate);
        dietaryFiber = findViewById(R.id.dietaryFiber);
        totalSugars = findViewById(R.id.totalSugars);
        protein = findViewById(R.id.protein);
        vitaminD = findViewById(R.id.vitaminD);
        vitaminA = findViewById(R.id.vitaminA);
        vitaminC = findViewById(R.id.vitaminC);
        calcium = findViewById(R.id.calcium);
        iron = findViewById(R.id.iron);
        potassium = findViewById(R.id.potassium);

        totalFatPDV = findViewById(R.id.totalFatPDV);
        saturatedFatPDV = findViewById(R.id.saturatedFatPDV);
        transFatPDV = findViewById(R.id.transFatPDV);
        cholesterolPDV = findViewById(R.id.cholesterolPDV);
        sodiumPDV = findViewById(R.id.sodiumPDV);
        totalCarbohydratePDV = findViewById(R.id.totalCarbohydratePDV);
        dietaryFiberPDV = findViewById(R.id.dietaryFiberPDV);
        totalSugarsPDV = findViewById(R.id.totalSugarsPDV);
        proteinPDV = findViewById(R.id.proteinPDV);
        vitaminDPDV = findViewById(R.id.vitaminDPDV);
        vitaminAPDV = findViewById(R.id.vitaminAPDV);
        vitaminCPDV = findViewById(R.id.vitaminCPDV);
        calciumPDV = findViewById(R.id.calciumPDV);
        ironPDV = findViewById(R.id.ironPDV);
        potassiumPDV = findViewById(R.id.potassiumPDV);

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


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AllergenAdapter allergenAdapter = new AllergenAdapter(ItemActivity.this, allergens);
            allergenListView.setAdapter(allergenAdapter);



            /*
            totalFat.setText(mixedWeight("Total Fat", nutrition.totalFat));
            saturatedFat.setText(mixedWeight("SaturatedFat", nutrition.saturatedFat));
            transFat.setText(mixedWeight("Trans Fat", nutrition.transFat));
            cholesterol.setText(mixedWeight("Cholesterol", nutrition.cholesterol));
            sodium.setText(mixedWeight("Sodium", nutrition.sodium));
            totalCarbohydrate.setText(mixedWeight("Total Carbohydrate", nutrition.totalCarbohydrate));
            dietaryFiber.setText(mixedWeight("Dietary Fiber", nutrition.dietaryFiber));
            totalSugars.setText(mixedWeight("Total Sugars", nutrition.totalSugars));
            protein.setText(mixedWeight("Protein", nutrition.protein));
            vitaminD.setText(mixedWeight("Vitamin D", nutrition.vitaminD));
            vitaminA.setText(mixedWeight("Vitamin A", nutrition.vitaminA));
            vitaminC.setText(mixedWeight("Vitamin C", nutrition.vitaminC));
            calcium.setText(mixedWeight("Calcium", nutrition.calcium));
            iron.setText(mixedWeight("Iron", nutrition.iron));
            potassium.setText(mixedWeight("Potassium", nutrition.potassium));

            totalFatPDV.setText(nutrition.totalFatPDV);
            saturatedFatPDV.setText(nutrition.saturatedFatPDV);
            transFatPDV.setText(nutrition.transFatPDV);
            cholesterolPDV.setText(nutrition.cholesterolPDV);
            sodiumPDV.setText(nutrition.sodiumPDV);
            totalCarbohydratePDV.setText(nutrition.totalCarbohydratePDV);
            dietaryFiberPDV.setText(nutrition.dietaryFiberPDV);
            totalSugarsPDV.setText(nutrition.totalSugarsPDV);
            proteinPDV.setText(nutrition.proteinPDV);
            vitaminDPDV.setText(nutrition.vitaminDPDV);
            vitaminAPDV.setText(nutrition.vitaminAPDV);
            vitaminCPDV.setText(nutrition.vitaminCPDV);
            calciumPDV.setText(nutrition.calciumPDV);
            ironPDV.setText(nutrition.ironPDV);
            potassiumPDV.setText(nutrition.potassiumPDV);
            */
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
