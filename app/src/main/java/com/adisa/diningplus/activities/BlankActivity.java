package com.adisa.diningplus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.adisa.diningplus.R;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int startHall = preferences.getInt("startHall", -1);
        Intent i = new Intent();
        if (startHall != -1) {
            i.setClass(getApplicationContext(), LocationActivity.class);
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
