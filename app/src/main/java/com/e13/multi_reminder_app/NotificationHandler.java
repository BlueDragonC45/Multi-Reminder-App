package com.e13.multi_reminder_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

class workerThread extends Thread {

    Context context;
    DatabaseHelper dbHelper;
    Settings settings = new Settings();

    workerThread(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public void run() {
        do {
            long timeTil = checkReminders();
            if (timeTil != 0) {
                timer(timeTil);
            } else {
                this.interrupt();
            }
        } while (!this.isInterrupted());
    }

    private long checkReminders() {
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
                if (dbHelper.isActive(reminder) == 1) {
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
        if (active && firstReminder != null) {
            createNotification("Active reminder", "Reminder " + firstReminder.name + " is active");
            return settings.getActiveDelay();
        } else if (!empty) {
            return lowestTime;
        } else {
            return 0;
        }
    }

    public void timer(long ms) {
        try {
            sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void createNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("Reminder", "Reminders", NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channel);

        Intent notifIntent = new Intent(context, ActiveRemindersActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Reminder")
                .setSmallIcon(R.mipmap.ic_launcher_mra)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent);

        manager.notify(0, builder.build());
    }

}

public class NotificationHandler extends Service {

    workerThread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("BRN", "App running in the background", NotificationManager.IMPORTANCE_LOW);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channel);

        Intent notificationIntent = new Intent(this, NotificationHandler.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this, "BRN")
                        .setContentTitle("MultiReminder app is running")
                        .setSmallIcon(R.mipmap.ic_launcher_mra)
                        .setContentIntent(pendingIntent)
                        .build();

        startForeground(120, notification);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thread == null) {
            thread = new workerThread(this);
            thread.start();
        } else if (thread.isAlive() || thread.getState().equals(Thread.State.TIMED_WAITING)) {
            thread.interrupt();
            thread = new workerThread(this);
            thread.start();
        }
        return START_STICKY;
    }

}
