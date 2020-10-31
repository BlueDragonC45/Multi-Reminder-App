package com.e13.multi_reminder_app;

import java.io.Serializable;

public class Reminder implements Serializable, Comparable<Reminder> {

    public String name;
    public long timeUntil;
    public int priority;
    public String tier;
    public String frequency;


    public Reminder(String name, long timeUntil, int priority, String tier, String frequency) {
        this.name = name;
        this.timeUntil = timeUntil;
        this.priority = priority;
        this.tier = tier;
        this.frequency = frequency;
}

    public Reminder(Reminder reminder, long timeUntil) {
        this.name = reminder.name;
        this.timeUntil = timeUntil;
        this.priority = reminder.priority;
        this.tier = reminder.tier;
        this.frequency = reminder.frequency;

    }

    @Override
    public int compareTo(Reminder reminder) {
        if (this.priority == reminder.priority) {
            if (this.timeUntil >= reminder.timeUntil) {
                return 1;
            } else {
                return -1;
            }
        } else if (this.priority > reminder.priority) {
            return -1;
        } else {
            return 1;
        }
    }

    public String toString() {
        return "" + name + ", " + timeUntil + ", " + priority + ", " + tier + ", " + frequency;
    }


}
