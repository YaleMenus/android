package com.adisa.diningplus;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class DiningHallActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbar;
    DiningDbHelper dbHelper;
    String hallName;
    int hallId;
    HashMap<String, ArrayList<FoodItem>> mealMap;
    HashMap<String, Integer> headerMap = new HashMap<>();
    ArrayList<Meal> mealList;
    MenuAdapter menuAdapter;
    ExpandableListView expandableListView;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences preferences;
    View emptyView;
    View loadingView;

    class FoodItem {
        int id;
        String name;
        boolean marked;

        FoodItem(int id, String name) {
            this.id = id;
            this.name = name;
            marked = false;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    class Meal {
        String startTime;
        String endTime;
        String name;

        Meal(String name, String startTime, String endTime) {
            this.name = name;
            this.endTime = endTime;
            this.startTime = startTime;
        }

        public String getName() {
            return name;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_hall);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coord_layout);

        headerMap.put("Berkeley", R.drawable.berkeley_header);
        headerMap.put("Branford", R.drawable.branford_header);
        headerMap.put("Grace Hopper", R.drawable.hopper_header);
        headerMap.put("Stiles", R.drawable.stiles_header);
        headerMap.put("Davenport", R.drawable.davenport_header);
        headerMap.put("Franklin", R.drawable.franklin_header);
        headerMap.put("Pauli Murray", R.drawable.murray_header);
        headerMap.put("Jonathan Edwards", R.drawable.je_header);
        headerMap.put("Morse", R.drawable.morse_header);
        headerMap.put("Pierson", R.drawable.pierson_header);
        headerMap.put("Saybrook", R.drawable.saybrook_header);
        headerMap.put("Silliman", R.drawable.silliman_header);
        headerMap.put("Timothy Dwight", R.drawable.td_header);
        headerMap.put("Trumbull", R.drawable.trumbull_header);

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
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPos, int childPos, long id) {
                FoodItem foodItem = (FoodItem) menuAdapter.getChild(groupPos, childPos);
                Intent i = new Intent();
                i.setClass(getApplicationContext(), ItemDetailActivity.class);
                i.putExtra("name", foodItem.getName());
                i.putExtra("id", foodItem.getId());
                startActivity(i);
                return true;
            }
        });

        Intent i = getIntent();
        dbHelper = new DiningDbHelper(getApplicationContext());
        hallName = i.getStringExtra("Name");
        hallId = i.getIntExtra("HallId", -1);
        collapsingToolbar.setTitle(hallName);
        ImageView header = (ImageView) findViewById(R.id.header);
        header.setImageDrawable(getResources().getDrawable(headerMap.get(hallName)));
        emptyView = findViewById(R.id.dining_hall_empty);
        loadingView = findViewById(R.id.dining_hall_progress);
        expandableListView.setEmptyView(emptyView);
        MenuTask menuTask = new MenuTask();
        menuTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dining_hall, menu);
        if (preferences.getInt("startHall", -1) == hallId) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_black_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                SharedPreferences.Editor editor = preferences.edit();
                if (preferences.getInt("startHall", -1) != hallId) {
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                    editor.putInt("startHall", hallId);
                    editor.putString("startHallName", hallName);
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    editor.putInt("startHall", -1);
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

    public static Date resetTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static void setMenu(DiningDbHelper dbHelper, int hallId) throws IOException, JSONException, ParseException {
        JSONArray menuData = MainActivity.getJSON("https://www.yaledining.org/fasttrack/menus.cfm?location=" +
                hallId + "&version=3");
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

    private class MenuTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("get", "start");
            expandableListView.setEmptyView(findViewById(R.id.dining_hall_progress));
            emptyView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Cursor result = dbHelper.getHall(hallId);
                Date lastUpdated = new Date();
                while (result.moveToNext()) {
                    String updateString = result.getString(result.getColumnIndex(DiningContract.DiningHall.LAST_UPDATED));
                    if (!updateString.equals("")) {
                        lastUpdated = DateFormatProvider.date.parse(updateString);
                    }
                }
                Date currentDate = resetTime(new Date());
                if (!dbHelper.itemInDb(DiningContract.MenuItem.TABLE_NAME, DiningContract.MenuItem.DINING_HALL, "" + hallId) ||
                        lastUpdated.compareTo(currentDate) != 0) {
                    setMenu(dbHelper, hallId);
                }
                dbHelper.updateTime(hallId);
            } catch (JSONException | ParseException | IOException e) {
                Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }


            Cursor result = dbHelper.getMenu(hallId);
            mealMap = new HashMap<>();
            mealList = new ArrayList<Meal>();
            while (result.moveToNext()) {
                String mealName = result.getString(result.getColumnIndex(DiningContract.MenuItem.MENU_NAME));
                ArrayList<FoodItem> newList;
                if (mealMap.containsKey(mealName)) {
                    newList = mealMap.get(mealName);
                } else {
                    newList = new ArrayList<>();
                    mealList.add(new Meal(mealName, result.getString(result.getColumnIndex(DiningContract.MenuItem.START_TIME)), result.getString(result.getColumnIndex(DiningContract.MenuItem.END_TIME))));
                }
                newList.add(new FoodItem(result.getInt(result.getColumnIndex(DiningContract.MenuItem.NUTRITION_ID)), result.getString(result.getColumnIndex(DiningContract.MenuItem.NAME))));
                mealMap.put(mealName, newList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            menuAdapter = new MenuAdapter(DiningHallActivity.this, mealList, mealMap);
            expandableListView.setAdapter(menuAdapter);
            if (menuAdapter.getGroupCount() > 0)
                expandableListView.expandGroup(0);
            expandableListView.setEmptyView(findViewById(R.id.dining_hall_empty));
            loadingView.setVisibility(View.GONE);
            TraitTask traitTask = new TraitTask();
            traitTask.execute();
            Log.d("get", "done");
        }
    }

    private class TraitTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("get", "start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (Meal meal : mealList) {
                    ArrayList<FoodItem> newList = new ArrayList<>();
                    for (FoodItem foodItem : mealMap.get(meal.getName())) {
                        ItemDetailActivity.setNutItem(dbHelper, foodItem.getId());
                        Cursor result = dbHelper.getNutritionItem(foodItem.getId());
                        while (result.moveToNext()) {
                            HashSet<String> traitList = (HashSet<String>) preferences.getStringSet("traitPrefs", new HashSet<String>());
                            for (String trait : traitList) {
                                if (result.getInt(result.getColumnIndex(trait.toLowerCase())) == 1) {
                                    foodItem.marked = true;
                                    Log.d("traitTask", "marked");
                                    break;
                                }
                            }
                        }
                        newList.add(foodItem);
                    }
                    mealMap.put(meal.getName(), newList);
                }
            } catch (JSONException | IOException e) {
                Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            menuAdapter.setMap(mealMap);
            Log.d("get", "done");
        }
    }
}
