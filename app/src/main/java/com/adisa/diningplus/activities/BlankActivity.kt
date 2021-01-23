package com.adisa.diningplus.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import com.adisa.diningplus.R

class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val defaultHallId = preferences.getString("default_hall_id", null)
        val i = Intent()
        if (defaultHallId != null) {
            i.setClass(applicationContext, HallActivity::class.java)
            i.putExtra("name", preferences.getString("default_hall_name", ""))
            i.putExtra("id", defaultHallId)
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(i)
                    .startActivities()
        } else {
            i.setClass(applicationContext, MainActivity::class.java)
            startActivity(i)
        }
    }
}