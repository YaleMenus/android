package com.adisa.diningplus.db

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.adisa.diningplus.R
import com.adisa.diningplus.activities.HallActivity

class DatabaseUpdateReceiver : BroadcastReceiver() {
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("receiver", "start")
        val itemName = intent.getStringExtra("itemName")
        val hallId = intent.getStringExtra("hallId")
        val hallName = intent.getStringExtra("hallName")
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                .setContentTitle("Favorite Dish")
                .setContentText("$itemName is being served today!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.primary))
        val resultIntent = Intent(context, HallActivity::class.java)
        resultIntent.putExtra("hallId", hallId)
        resultIntent.putExtra("hallName", hallName)
        val stackBuilder = TaskStackBuilder.create(context)
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HallActivity::class.java)
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build())
    }
}