package com.adisa.diningplus.db;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.adisa.diningplus.R;
import com.adisa.diningplus.activities.LocationActivity;

public class DatabaseUpdateReceiver extends BroadcastReceiver {

    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "start");
        String itemName = intent.getStringExtra("itemName");
        int locationId = intent.getIntExtra("locationId", -1);
        String locationName = intent.getStringExtra("locationName");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                        .setContentTitle("Favorite Dish")
                        .setContentText(itemName + " is being served today!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        Intent resultIntent = new Intent(context, LocationActivity.class);
        resultIntent.putExtra("locationId", locationId);
        resultIntent.putExtra("locationName", locationName);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LocationActivity.class);
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