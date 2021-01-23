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
        String defaultHallId = preferences.getString("default_hall_id", null);
        Intent i = new Intent();
        if (defaultHallId != null) {
            i.setClass(getApplicationContext(), HallActivity.class);
            i.putExtra("name", preferences.getString("default_hall_name", ""));
            i.putExtra("id", defaultHallId);
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(i)
                    .startActivities();
        } else {
            i.setClass(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
}
