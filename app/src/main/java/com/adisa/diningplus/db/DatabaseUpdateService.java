package com.adisa.diningplus.db;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adisa.diningplus.db.entities.Hall;
import com.adisa.diningplus.network.API;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

public class DatabaseUpdateService extends JobService {
    DatabaseClient db;
    API api;
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
            List<Hall> halls = db.getDB().hallDao().getAll();
            for (Hall hall : halls) {
                /*
                api.getMeals(location.id);
                List<Meal> = dbHelper.getMeals(id);
                while (menu.moveToNext()) {
                    if (preferences.getStringSet("followed_items", new HashSet<String>()).contains(menu.getString(menu.getColumnIndex(DatabaseContract.Item.NAME)))) {
                        Intent localIntent = new Intent(BROADCAST_ACTION);
                        localIntent.putExtra("itemName", menu.getString(menu.getColumnIndex(DatabaseContract.Item.NAME)));
                        localIntent.putExtra("hallId", menu.getInt(menu.getColumnIndex(DatabaseContract.Item.LOCATION_ID)));
                        localIntent.putExtra("hallName", result.getString(result.getColumnIndex(DatabaseContract.Location.NAME)));
                        sendBroadcast(localIntent);
                        Log.d("service", "broadcast");
                        return false;
                    }
                }
                 */
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
