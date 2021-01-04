package com.adisa.diningplus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Meal;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.adisa.diningplus.R;
import com.adisa.diningplus.adapters.MenuAdapter;
import com.adisa.diningplus.network.API;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    API api;

    CollapsingToolbarLayout collapsingToolbar;
    String locationName;
    String locationCode;
    int locationId;
    HashMap<String, List<Item>> mealItems;
    HashMap<String, Integer> headerMap = new HashMap<>();
    List<Meal> meals;
    MenuAdapter menuAdapter;
    ExpandableListView expandableListView;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences preferences;
    View emptyView;
    View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        api = new API(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coord_layout);

        headerMap.put("BK", R.drawable.berkeley_header);
        headerMap.put("BR", R.drawable.branford_header);
        headerMap.put("GH", R.drawable.hopper_header);
        headerMap.put("ES", R.drawable.stiles_header);
        headerMap.put("DC", R.drawable.davenport_header);
        headerMap.put("BF", R.drawable.franklin_header);
        headerMap.put("MY", R.drawable.murray_header);
        headerMap.put("JE", R.drawable.je_header);
        headerMap.put("MC", R.drawable.morse_header);
        headerMap.put("PC", R.drawable.pierson_header);
        headerMap.put("SY", R.drawable.saybrook_header);
        headerMap.put("SM", R.drawable.silliman_header);
        headerMap.put("TD", R.drawable.td_header);
        headerMap.put("TC", R.drawable.trumbull_header);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandableListView.setNestedScrollingEnabled(true);
        } else {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) coordinatorLayout.getLayoutParams();
            TypedValue tv = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            params.bottomMargin = getResources().getDimensionPixelSize(tv.resourceId);
            coordinatorLayout.setLayoutParams(params);
        }
        expandableListView.setGroupIndicator(null);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int mealPosition, int itemPosition, long id) {
                Item item = (Item) menuAdapter.getChild(mealPosition, itemPosition);
                Intent i = new Intent();
                i.setClass(getApplicationContext(), ItemActivity.class);
                i.putExtra("name", item.name);
                i.putExtra("id", item.id);
                startActivity(i);
                return true;
            }
        });

        Intent i = getIntent();

        locationName = i.getStringExtra("name");
        locationCode = i.getStringExtra("code");
        locationId = i.getIntExtra("id", -1);
        collapsingToolbar.setTitle(locationName);
        ImageView header = (ImageView) findViewById(R.id.header);
        header.setImageDrawable(getResources().getDrawable(headerMap.get(locationCode)));
        emptyView = findViewById(R.id.location_empty);
        loadingView = findViewById(R.id.location_progress);
        expandableListView.setEmptyView(emptyView);
        MealsTask mealsTask = new MealsTask();
        mealsTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_location, menu);
        if (preferences.getInt("startLocation", -1) == locationId) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_black_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                SharedPreferences.Editor editor = preferences.edit();
                if (preferences.getInt("startLocation", -1) != locationId) {
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                    editor.putInt("startLocation", locationId);
                    editor.putString("startLocationName", locationName);
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    editor.putInt("startLocation", -1);
                }
                editor.apply();
                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MealsTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("get", "start");
            expandableListView.setEmptyView(findViewById(R.id.location_progress));
            emptyView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Meal> meals = null;
            try {
                meals = api.getLocationMeals(locationId, new Date());
            } catch (JSONException | IOException e) {
                Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
            mealItems = new HashMap<>();
            for (Meal meal : meals) {
                List<Item> items = null;
                try {
                    items = api.getMealItems(meal.id);
                } catch (JSONException | IOException e) {
                    Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                mealItems.put(meal.name, items);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            menuAdapter = new MenuAdapter(LocationActivity.this, meals, mealItems);
            expandableListView.setAdapter(menuAdapter);
            if (menuAdapter.getGroupCount() > 0)
                expandableListView.expandGroup(0);
            expandableListView.setEmptyView(findViewById(R.id.location_empty));
            AllergenTask allergenTask = new AllergenTask();
            allergenTask.execute();
            Log.d("get", "done");
        }
    }

    private class AllergenTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("get", "start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashSet<String> allergens = (HashSet<String>) preferences.getStringSet("allergens", new HashSet<String>());
            for (Meal meal : meals) {
                ArrayList<Item> newList = new ArrayList<>();
                for (Item item : mealItems.get(meal.name)) {
                    for (String allergen : allergens) {
                        try {
                            if ((boolean) item.getClass().getField(allergen).get(item)) {
                                item.allergenic = true;
                                break;
                            }
                        } catch (NoSuchFieldException e) {
                            // Should never happen
                        } catch (IllegalAccessException e) {
                            // Should never happen
                        }
                    }
                    newList.add(item);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            menuAdapter.setItems(mealItems);
            loadingView.setVisibility(View.GONE);
            Log.d("get", "done");
        }
    }
}