package com.e13.multi_reminder_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class NotificationHandler extends AppCompatActivity {

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thinking_screen);

        Reminder firstReminder = null;
        boolean active = false;
        boolean empty = false;
        long lowestTime = Long.MAX_VALUE;
        Cursor res = dbHelper.getAllData();


        while (res.moveToNext()) {
            if(res.getCount() == 0) {
                empty = true;
                break;
            } else {
                Reminder reminder = (Reminder) dbHelper.readByte(res.getBlob(1));
                if ((reminder.timeUntil - System.currentTimeMillis()) < 10000) {
                    active = true;
                    dbHelper.updateData(res.getInt(0), reminder, res.getInt(3), 1);
                    firstReminder = reminder;
                } else if (res.getInt(4) == 1) {
                    active = true;
                } else {
                    if ((reminder.timeUntil - System.currentTimeMillis()) < lowestTime) {
                        lowestTime = (reminder.timeUntil - System.currentTimeMillis());
                    }
                }
            }
        }
        Intent intent = new Intent(NotificationHandler.this, NotificationHandler.class);
        if (active && firstReminder != null) {
            createNotification("Active reminder", "Reminder " + firstReminder.name + " is active");
            createDelayedIntent(intent, 30000);
            finish();
        } else if (!empty) {
            createDelayedIntent(intent, lowestTime);
            startActivity(new Intent(NotificationHandler.this, MainPageActivity.class));
            finish();
        } else {
            startActivity(new Intent(NotificationHandler.this, MainPageActivity.class));
            finish();
        }
    }

    public void createDelayedIntent(final Intent intent, long time) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, time);
    }

    public void createNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("Reminder", "Reminders", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Reminder")
            .setSmallIcon(R.mipmap.ic_launcher_mra)
            .setContentTitle(title)
            .setContentText(message);

        Intent notifIntent = new Intent(this, NotificationHandler.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        manager.notify(0, builder.build());
    }

}
