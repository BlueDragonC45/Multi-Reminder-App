package com.e13.multi_reminder_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "reminders.db";
    public static final String TABLE_NAME = "reminder_table";
    public static final String COL_0 = "id";
    public static final String COL_1 = "reminder";
//    public static final String COL_1 = "name";
//    public static final String COL_2 = "time";
//    public static final String COL_3 = "priority";
//    public static final String COL_4 = "tier";
//    public static final String COL_5 = "frequency";
    public static final String COL_6 = "attachment";
    public static final String COL_7 = "this_week_flag";
    public static final String COL_8 = "active";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, REMINDER BLOB, ATTACHMENT INTEGER, " +
                "THIS_WEEK_FLAG INTEGER, ACTIVE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
