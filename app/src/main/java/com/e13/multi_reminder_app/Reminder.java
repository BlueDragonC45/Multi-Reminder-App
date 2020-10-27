package com.e13.multi_reminder_app;

public class Reminder {

    public String name;
    public int month;
    public int year;
    public int day;
    public int hour;
    public int minute;
    public int priority;
    public String tier;
    public String frequency;


    public Reminder(String name, int year, int month, int day, int hour, int minute, int priority, String tier, String frequency) {
        this.name = name;
        this.month = month;
        this.year = year;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.priority = priority;
        this.tier = tier;
        this.frequency = frequency;
    }

    public Reminder getById(int id) {
        Reminder rmd = new Reminder(name, year, month, day, hour, minute, priority, tier, frequency);
        return rmd;
    }



}
