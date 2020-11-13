package com.e13.multi_reminder_app;

public class Settings {

    private static long snoozeTime = -1;
    private static long activeDelay = -1;

    public Settings() {
        if (snoozeTime == -1) {
            snoozeTime = 300000;
        }
        if (activeDelay == -1) {
            activeDelay = 60000;
        }
    }

    public long getSnoozeTime() {
        return snoozeTime;
    }

    public long getActiveDelay() {
        return activeDelay;
    }

    public void setSnoozeTime(long time) {
        snoozeTime = time;
    }

    public void setActiveDelay(long time) {
        activeDelay = time;
    }
}
