package com.adisa.diningplus.db;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.adisa.diningplus.R;
import com.adisa.diningplus.activities.DiningHallActivity;

/**
 * Created by Adisa on 5/1/2017.
 */

public class DatabaseUpdateReceiver extends BroadcastReceiver {

    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "start");
        String itemName = intent.getStringExtra("itemName");
        int diningHall = intent.getIntExtra("diningHall", -1);
        String hallName = intent.getStringExtra("hallName");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                        .setContentTitle("Favorite Dish")
                        .setContentText(itemName + " is being served today!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        Intent resultIntent = new Intent(context, DiningHallActivity.class);
        resultIntent.putExtra("HallId", diningHall);
        resultIntent.putExtra("Name", hallName);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(DiningHallActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}