package com.e13.multi_reminder_app;

public class Reminder {

    public String name;
//    public ???? time;
    public int priority;
    public String tier;
    public String frequency;


    public Reminder(String name, /*??? time,*/ int priority, String tier, String frequency) {
        this.name = name;
//        this.time = time;
        this.priority = priority;
        this.tier = tier;
        this.frequency = frequency;
    }

    public Reminder getById(int id) {
        Reminder rmd = new Reminder(name, /*time,*/ priority, tier, frequency);
        return rmd;
    }



}
