package com.adisa.diningplus.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.adisa.diningplus.db.DatabaseClient;
import com.adisa.diningplus.db.entities.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adisa.diningplus.db.DatabaseUpdateService;
import com.adisa.diningplus.network.DiningAPI;
import com.adisa.diningplus.adapters.MainListAdapter;
import com.adisa.diningplus.R;
import com.adisa.diningplus.fragments.AllergenDialogFragment;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    DatabaseClient db;
    DiningAPI api;

    private MainListAdapter adapter;
    ArrayList<LocationItem> openLocations = new ArrayList<>();
    ArrayList<LocationItem> closedLocations = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;
    CoordinatorLayout coordinatorLayout;

    GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient fused;
    android.location.Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);

        db = new DatabaseClient(this);
        api = new DiningAPI(db);

        adapter = new MainListAdapter(this);
        ListView mainList = findViewById(R.id.halls);
        mainList.setAdapter(adapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), LocationActivity.class);
                i.putExtra("Name", adapter.getItem(position).name);
                i.putExtra("LocationId", adapter.getItem(position).id);
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

        fused = LocationServices.getFusedLocationProviderClient(this);

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

        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("traits");
            if (prev == null) {
                DialogFragment traitDialog = new AllergenDialogFragment();
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
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fused = LocationServices.getFusedLocationProviderClient(this);
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
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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

    public static class LocationItem {
        public String name;
        public int capacity;
        public int id;
        public double latitude;
        public double longitude;
        public double distance;
        public boolean open;

        LocationItem(String name, int capacity, double latitude, double longitude, int id, boolean open) {
            this.name = name;
            this.capacity = capacity;
            this.latitude = latitude;
            this.longitude = longitude;
            this.id = id;
            this.open = open;
        }

        void setDistance(android.location.Location location) {
            if (location != null) {
                android.location.Location loc = new android.location.Location("");
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                // Convert m -> km
                distance = location.distanceTo(loc) / 1000;
            }
        }
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            closedLocations = new ArrayList<>();
            openLocations = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                api.getLocations();
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(coordinatorLayout, R.string.web_error, Snackbar.LENGTH_LONG).show();
            }
            List<Location> locations = db.getDB().locationDao().getAll();
            for (Location location : locations) {
                LocationItem item = new LocationItem(location.name,
                                                     location.capacity,
                                                     location.latitude,
                                                     location.longitude,
                                                     location.id,
                                                     location.isOpen);
                item.setDistance(currentLocation);
                if (item.open) {
                    openLocations.add(item);
                } else {
                    closedLocations.add(item);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("URI", "done");
            adapter.setLists(openLocations, closedLocations);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    }
}
