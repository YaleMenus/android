package com.adisa.diningplus.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.adisa.diningplus.R
import com.adisa.diningplus.adapters.MainListAdapter
import com.adisa.diningplus.db.DatabaseUpdateService
import com.adisa.diningplus.fragments.DietaryRestrictionDialogFragment
import com.adisa.diningplus.fragments.FollowDialogFragment
import com.adisa.diningplus.fragments.StatusDialogFragment
import com.adisa.diningplus.network.API
import com.adisa.diningplus.network.entities.Hall
import com.firebase.jobdispatcher.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity(), ConnectionCallbacks, OnConnectionFailedListener {
    var api: API? = null
    private var adapter: MainListAdapter? = null
    var openHalls = ArrayList<Hall>()
    var closedHalls = ArrayList<Hall>()
    var swipeContainer: SwipeRefreshLayout? = null
    var coordinatorLayout: CoordinatorLayout? = null
    var mGoogleApiClient: GoogleApiClient? = null
    private var fused: FusedLocationProviderClient? = null
    var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.AppTheme)
        api = API(this)
        adapter = MainListAdapter(this)
        val mainList = findViewById<ListView>(R.id.halls)
        mainList.adapter = adapter
        mainList.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            val i = Intent()
            i.setClass(applicationContext, HallActivity::class.java)
            i.putExtra("name", adapter!!.getItem(position).name)
            i.putExtra("id", adapter!!.getItem(position).id)
            startActivity(i)
        }
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer!!.setOnRefreshListener(OnRefreshListener {
            val updateTask = UpdateTask()
            updateTask.execute()
        })
        swipeContainer!!.setColorSchemeResources(R.color.accent)
        coordinatorLayout = findViewById(R.id.snackbar)
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        fused = LocationServices.getFusedLocationProviderClient(this)

        // Registers the DownloadStateReceiver and its intent filters
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val myJob = dispatcher.newJobBuilder() // the JobService that will be called
                .setService(DatabaseUpdateService::class.java) // uniquely identifies the job
                .setTag("database-update") // one-off job
                .setRecurring(true) // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT) // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(18000, 43200)) // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true) // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL) // constraints that need to be satisfied for the job to run
                .build()
        dispatcher.mustSchedule(myJob)
        val updateTask = UpdateTask()
        updateTask.execute()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getBoolean("firstRun", true)) {
            val editor = preferences.edit()
            editor.putBoolean("firstRun", false)
            editor.apply()
            val prev = supportFragmentManager.findFragmentByTag("traits")
            if (prev == null) {
                val traitDialog: DialogFragment = DietaryRestrictionDialogFragment()
                traitDialog.show(supportFragmentManager, "traits")
            }
        }
        val statusTask = StatusTask()
        statusTask.execute()
    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        super.onStop()
    }

    override fun onResume() {
        adapter!!.notifyDataSetChanged()
        super.onResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fused = LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
        } else {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_settings -> {
                val i = Intent()
                i.setClass(applicationContext, SettingsActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class StatusTask : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val status = api!!.getStatus()
            val version = 1
            if (version < status.minVersion!!) {
                val statusDialog: DialogFragment = StatusDialogFragment("You're using an outdated version of Yale Menus that is no longer supported. Please update the app through the Play Store to avoid unexpected behavior.")
                statusDialog.show(supportFragmentManager, "statusVersion")
            }
            // TODO: JSONObject.getString returns "null" for null values. Figure out how to convert to plain null.
            if (status.message != null && status.message!! != "null" && status.message!!.isNotEmpty()) {
                System.out.println(status.message!!.substring(2))
                System.out.println(status.message != null)
                val statusDialog: DialogFragment = StatusDialogFragment(status.message!!)
                statusDialog.show(supportFragmentManager, "statusMessage")
            }
            return null
        }
    }

    private inner class UpdateTask : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            closedHalls = ArrayList()
            openHalls = ArrayList()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val halls: List<Hall>
            halls = try {
                api!!.getHalls()
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(coordinatorLayout!!, R.string.web_error, Snackbar.LENGTH_LONG).show()
                return null
            }
            for (hall in halls) {
                hall.setDistance(currentLocation)
                if (hall.open) {
                    openHalls.add(hall)
                } else {
                    closedHalls.add(hall)
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            Log.d("URI", "done")
            adapter!!.setLists(openHalls, closedHalls)
            adapter!!.notifyDataSetChanged()
            swipeContainer!!.isRefreshing = false
        }
    }
}