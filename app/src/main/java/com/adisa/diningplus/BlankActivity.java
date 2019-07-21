package com.adisa.diningplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int startHall = preferences.getInt("startHall", -1);
        Intent i = new Intent();
        if (startHall != -1) {
            i.setClass(getApplicationContext(), DiningHallActivity.class);
            i.putExtra("Name", preferences.getString("startHallName", ""));
            i.putExtra("HallId", startHall);
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(i)
                    .startActivities();
        } else {
            i.setClass(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
}
