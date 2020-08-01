package com.example.uctregisterbookapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class Notifications {

    private static final int CHANNEL_ID = 0;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;

    public Notifications(Context context){


        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("UCT Register Book")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


    }

    public void show(String content){
        builder.setContentText(content);
        manager.notify(CHANNEL_ID, builder.build());

    }

}
