package com.adisa.diningplus.db

import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import com.adisa.diningplus.network.API
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

class DatabaseUpdateService : JobService() {
    var api: API? = null
    var preferences: SharedPreferences? = null
    var mDownloadStateReceiver: DatabaseUpdateReceiver? = null
    override fun onCreate() {
        mDownloadStateReceiver = DatabaseUpdateReceiver()
        registerReceiver(mDownloadStateReceiver, IntentFilter(BROADCAST_ACTION))
        super.onCreate()
    }

    override fun onDestroy() {
        unregisterReceiver(mDownloadStateReceiver)
        super.onDestroy()
    }

    override fun onStartJob(job: JobParameters): Boolean {
        // Do some work here
        val updateTask = UpdateTask()
        updateTask.execute()
        return false // Answers the question: "Is there still work going on?"
    }

    override fun onStopJob(job: JobParameters): Boolean {
        return false // Answers the question: "Should this job be retried?"
    }

    private inner class UpdateTask : AsyncTask<Void?, Boolean?, Boolean>() {

        override fun doInBackground(vararg params: Void?): Boolean {
            preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            /*
            val halls = db!!.db.hallDao().getAll()
            for (hall in halls) {
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
            }
             */
            return false
        }

        override fun onPostExecute(result: Boolean) {
            if (!result) {
                Log.d("service", "done")
            } else {
                Log.d("service", "failed")
            }
        }
    }

    companion object {
        const val BROADCAST_ACTION = "com.adisa.diningplus.threadsample.BROADCAST"
    }
}