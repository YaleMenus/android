package com.adisa.diningplus;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;

public class DatabaseUpdateService extends JobService {
    DiningDbHelper dbHelper;
    SharedPreferences preferences;
    DatabaseUpdateReceiver mDownloadStateReceiver;
    public static final String BROADCAST_ACTION =
            "com.adisa.diningplus.threadsample.BROADCAST";

    @Override
    public void onCreate() {
        mDownloadStateReceiver = new DatabaseUpdateReceiver();
        registerReceiver(mDownloadStateReceiver, new IntentFilter(BROADCAST_ACTION));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mDownloadStateReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        return false; // Answers the question: "Should this job be retried?"
    }

    private class UpdateTask extends AsyncTask<Void, Boolean, Boolean> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("service", "start");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            dbHelper = new DiningDbHelper(getApplicationContext());
            Cursor result = dbHelper.getHalls();
            while (result.moveToNext()) {
                int id = result.getInt(result.getColumnIndex(DiningContract.DiningHall._ID));
                try {
                    DiningHallActivity.setMenu(dbHelper, id);
                } catch (IOException | JSONException | ParseException e) {
                    e.printStackTrace();
                    return true;
                }
                dbHelper.updateTime(id);
                Cursor menu = dbHelper.getMenu(id);
                while (menu.moveToNext()) {
                    if (preferences.getStringSet("followedItems", new HashSet<String>()).contains(menu.getString(menu.getColumnIndex(DiningContract.MenuItem.NAME)))) {
                        Intent localIntent = new Intent(BROADCAST_ACTION);
                        localIntent.putExtra("itemName", menu.getString(menu.getColumnIndex(DiningContract.MenuItem.NAME)));
                        localIntent.putExtra("diningHall", menu.getInt(menu.getColumnIndex(DiningContract.MenuItem.DINING_HALL)));
                        localIntent.putExtra("hallName", result.getString(result.getColumnIndex(DiningContract.DiningHall.NAME)));
                        sendBroadcast(localIntent);
                        Log.d("service", "broadcast");
                        return false;
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Log.d("service", "done");
            } else {
                Log.d("service", "failed");
            }
        }
    }
}
