package com.adisa.diningplus;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    DiningDbHelper dbHelper;
    private MainListAdapter adapter;
    ArrayList<HallItem> openList = new ArrayList<>();
    ArrayList<HallItem> closedList = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;
    Location currentLocation;
    GoogleApiClient mGoogleApiClient;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);

        adapter = new MainListAdapter(this);
        ListView mainList = findViewById(R.id.hallList);
        mainList.setAdapter(adapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), DiningHallActivity.class);
                i.putExtra("Name", adapter.getItem(position).name);
                i.putExtra("HallId", adapter.getItem(position).id);
                startActivity(i);
            }
        });
        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateTask updateTask = new UpdateTask();
                updateTask.execute();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        coordinatorLayout = findViewById(R.id.snackbar);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        // Registers the DownloadStateReceiver and its intent filters
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(DatabaseUpdateService.class)
                // uniquely identifies the job
                .setTag("database-update")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(18000, 43200))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .build();

        dispatcher.mustSchedule(myJob);

        dbHelper = new DiningDbHelper(getApplicationContext());
        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("traits");
            if (prev == null) {
                DialogFragment traitDialog = new TraitDialogFragment();
                traitDialog.show(getSupportFragmentManager(), "traits");
            }
        }


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                Intent i = new Intent();
                i.setClass(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class HallItem {
        String name;
        int occupancy;
        int id;
        double latitude;
        double longitude;
        double distance;
        boolean open;

        HallItem(String name, int occupancy, double latitude, double longitude, int id, boolean open) {
            this.name = name;
            this.occupancy = occupancy;
            this.latitude = latitude;
            this.longitude = longitude;
            this.id = id;
            this.open = open;
        }

        void setDistance(Location location) {
            if (location != null) {
                Location loc = new Location("");
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                // Convert m -> km
                distance = location.distanceTo(loc) / 1000;
            }
        }
    }

    public static JSONArray getJSON(String urlString) throws IOException, JSONException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        //read all the data
        InputStream inputStream = urlConnection.getInputStream();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return new JSONObject(buffer.toString()).getJSONArray("DATA");
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            closedList = new ArrayList<>();
            openList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("URI", "making request");
                JSONArray resultData = getJSON("https://www.yaledining.org/fasttrack/locations.cfm?version=3");
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
                        if (!dbHelper.itemInDb(DiningContract.DiningHall.TABLE_NAME, DiningContract.DiningHall._ID, values.getAsInteger(DiningContract.MenuItem._ID).toString())) {
                            values.put(DiningContract.DiningHall.LAST_UPDATED, "");
                            dbHelper.insertHall(values);
                        } else {
                            dbHelper.updateHall(values);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("URI", "URI was invalid or API request failed");
                e.printStackTrace();
                Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
            }
            Cursor result = dbHelper.getHalls();
            while (result.moveToNext()) {
                HallItem newItem = new HallItem(result.getString(result.getColumnIndex(DiningContract.DiningHall.NAME)),
                        result.getInt(result.getColumnIndex(DiningContract.DiningHall.CAPACITY)),
                        result.getDouble(result.getColumnIndex(DiningContract.DiningHall.LATITUDE)),
                        result.getDouble(result.getColumnIndex(DiningContract.DiningHall.LONGITUDE)),
                        result.getInt(result.getColumnIndex(DiningContract.DiningHall._ID)),
                        result.getInt(result.getColumnIndex(DiningContract.DiningHall.IS_CLOSED)) == 0);
                newItem.setDistance(currentLocation);
                if (newItem.open) {
                    openList.add(newItem);
                } else {
                    closedList.add(newItem);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("URI", "done");
            adapter.setLists(openList, closedList);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    }
}
