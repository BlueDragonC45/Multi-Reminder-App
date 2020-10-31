package com.e13.multi_reminder_app.RecyclerViewParts;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.DatabaseHelper;
import com.e13.multi_reminder_app.HelperMethods;
import com.e13.multi_reminder_app.R;
import com.e13.multi_reminder_app.Reminder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ManageActiveReminders extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private ArrayList<String> activeReminders = new ArrayList<>();
    RecyclerViewAdapterActive adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_list);

        adapter = new RecyclerViewAdapterActive(activeReminders, this);
        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_active);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void addAll(ArrayList<String> list){
        activeReminders.clear();
        activeReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        ArrayList<String> dataList = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        while (res.moveToNext()) {
            if(res.getCount() == 0) {
                break;
            } else {
                Reminder reminder = (Reminder) dbHelper.readByte(res.getBlob(1));
                if (res.getInt(4) == 1) {
                    calendar.setTimeInMillis(reminder.timeUntil);
                    String priority = helper.getPriority(reminder.priority);
                    String msg = reminder.name + "," + dateFormat.format(calendar.getTime()) + "," + priority + ","
                            + reminder.frequency + "," + res.getInt(0) + "," + reminder.timeUntil + "," + reminder.priority + "," + reminder.tier;
                    dataList.add(msg);
                }
            }
        }
        addAll(dataList);
    }


}
