package com.e13.multi_reminder_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationHandler extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thinking_screen);
        System.out.println("This is getting called right?");
        boolean active = false;
        boolean empty = false;
        int counter = 1;
        long lowestTime = Long.MAX_VALUE;
        Cursor res = dbHelper.getAllData();
        while (res.moveToNext()) {
            if(res.getCount() == 0) {
                empty = true;
                break;
            } else {
                Reminder reminder = (Reminder) dbHelper.readByte(res.getBlob(1));
                if (reminder.timeUntil < 10000) {
                    active = true;
                    dbHelper.updateData(counter, reminder, res.getInt(3), 1);
                } else if (res.getInt(4) == 1) {
                    active = true;
                } else {
                    if (reminder.timeUntil < lowestTime) {
                        lowestTime = reminder.timeUntil;
                    }
                }
            }
        }
        Intent intent = new Intent(NotificationHandler.this, NotificationHandler.class);
        if (active) {
            createDelayedIntent(intent, 30000);
            finish();
        } else if (!empty) {
            System.out.println("And it makes it here, with a lowest time of " + lowestTime);
            createDelayedIntent(intent, lowestTime);
            startActivity(new Intent(NotificationHandler.this, MainPageActivity.class));
        } else {
            startActivity(new Intent(NotificationHandler.this, MainPageActivity.class));
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

}
