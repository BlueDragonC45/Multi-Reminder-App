package com.e13.multi_reminder_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

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
    public static final String COL_2 = "attachment";
    public static final String COL_3 = "this_week_flag";
    public static final String COL_4 = "active";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
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

    public byte[] makeByte(Object object) {
        try {
            ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
            ObjectOutputStream ObjOutputStream = new ObjectOutputStream(OutputStream);
            ObjOutputStream.writeObject(object);
            byte[] objectAsBytes = OutputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(objectAsBytes);
            return objectAsBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object readByte(byte[] data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            ObjectInputStream ObjInputStream = new ObjectInputStream(inputStream);
            return ObjInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertData(Reminder reminder, int attachment) {
        SQLiteDatabase db = this.getWritableDatabase();
        int this_week_flag;
        if (reminder.timeUntil < 604800001) {
            this_week_flag = 1;
        } else {
            this_week_flag = 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, makeByte(reminder));
        contentValues.put(COL_2, attachment);
        contentValues.put(COL_3, this_week_flag);
        contentValues.put(COL_4, 0);
        System.out.println(Arrays.toString(makeByte(reminder)));

        long result = db.insert(TABLE_NAME, null, contentValues);

//        db.close();
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
//        db.close();
        return res;
    }

    public boolean updateData(int id, Reminder reminder, int attachment, int active) {
        SQLiteDatabase db = this.getWritableDatabase();
        int this_week_flag;
        if (reminder.timeUntil < 604800001) {
            this_week_flag = 1;
        } else {
            this_week_flag = 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0, id);
        contentValues.put(COL_1, makeByte(reminder));
        contentValues.put(COL_2, attachment);
        contentValues.put(COL_3, this_week_flag);
        contentValues.put(COL_4, active);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { String.valueOf(id) });
//        db.close();
        return true;
    }

    public Integer deleteData (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer integer= db.delete(TABLE_NAME, "ID = ?", new String[] { String.valueOf(id) });
//        db.close();
        return integer;
    }

    public Reminder getById(int id) throws  IndexOutOfBoundsException{   //This will have to be modified for attachments
        Cursor res = getAllData();
        while (res.moveToNext()) {
            if (id == res.getInt(0)) {
                return (Reminder) readByte(res.getBlob(2));
            }
        }
        throw new IndexOutOfBoundsException("Id not found");
    }

}
