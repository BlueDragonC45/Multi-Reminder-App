package com.e13.multi_reminder_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.RecyclerViewParts.RecyclerViewAdapterAttachments;
import com.e13.multi_reminder_app.RecyclerViewParts.mSpinnerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddingAttachmentsActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<String> tiersList = new ArrayList<>();
    ArrayList<String> prioritiesList = new ArrayList<>();
    ArrayList<String> recyclerList = new ArrayList<>();
    RecyclerViewAdapterAttachments recyclerAdapter;
    String selTier = "None";
    String selPriority = "None";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.attachments_list);

            final CheckBox checkBox = findViewById(R.id.checkBoxAttachments);
            final LinearLayout filters = findViewById(R.id.searchFilters);
            final Spinner tiers = findViewById(R.id.spinnerTier);
            final Spinner priority = findViewById(R.id.spinnerPriority);
            initLists();
            mSpinnerAdapter AdapterTiers = new mSpinnerAdapter(this, tiersList);
            mSpinnerAdapter AdapterPriorities = new mSpinnerAdapter(this, prioritiesList);
            tiers.setAdapter(AdapterTiers);
            priority.setAdapter(AdapterPriorities);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        filters.setVisibility(View.VISIBLE);
                        initRecyclerView(selTier, selPriority);
                    } else {
                        filters.setVisibility(View.GONE);
                        initRecyclerViewAll();
                    }
                }
            });

            tiers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String clickedItem = (String) parent.getItemAtPosition(position);
                    selTier = clickedItem;
                    initRecyclerView(clickedItem, selPriority);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { selTier = "None"; } });

            priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String clickedItem = (String) parent.getItemAtPosition(position);
                    selPriority = clickedItem;
                    initRecyclerView(selTier, clickedItem);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { selPriority = "None"; } });

            recyclerAdapter = new RecyclerViewAdapterAttachments(recyclerList, this);
            initRecyclerView(selTier, selPriority);
            RecyclerView recyclerView = findViewById(R.id.recycler_view_attachments);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        }

    private void initLists() {
        tiersList.add("None");
        tiersList.add("Macro");
        tiersList.add("Meso");
        tiersList.add("Micro");
        prioritiesList.add("None");
        prioritiesList.add("Urgent");
        prioritiesList.add("High");
        prioritiesList.add("Medium");
        prioritiesList.add("Low");
    }

    public void addAll(ArrayList<String> list){
        recyclerList.clear();
        recyclerList.addAll(list);
        recyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(String selTier, String selPriority) {
        if (selTier.equals("None") && selPriority.equals("None")) {
            initRecyclerViewAll();
        } else if (selTier.equals("None")) {
            initRecyclerViewPriority(selPriority);
        } else if (selPriority.equals("None")) {
            initRecyclerViewTier(selTier);
        } else {
            ArrayList<String> dataList = new ArrayList<>();
            ArrayList<triplicate> sorter = new ArrayList<>();
            Cursor res = dbHelper.getAllData();
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

            if (res.getCount() == 0) {

            } else {
                while (res.moveToNext()) {
                    sorter.add(new triplicate((Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(4)));
                    sorter.sort(null);
                }
                for (int i = 0; i < sorter.size(); i++) {
                    Reminder reminder = sorter.get(i).reminder;
                    if ((sorter.get(i).reminder.tier).equals(selTier.toUpperCase()) && (helper.getPriority(sorter.get(i).reminder.priority)).equals(selPriority)) {
                        calendar.setTimeInMillis(reminder.timeUntil);
                        String priority = helper.getPriority(reminder.priority);
                        String msg = reminder.name + "," + dateFormat.format(calendar.getTime()) + "," + priority + ","
                                + reminder.frequency + "," + sorter.get(i).id + "," + reminder.timeUntil + "," + reminder.priority + "," + reminder.tier;
                        dataList.add(msg);
                    }
                }
            }
            if (dataList.size() == 0) {
                Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
            }
            addAll(dataList);
        }
    }

    private void initRecyclerViewAll() {
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<triplicate> sorter = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        if (res.getCount() == 0) {

        } else {
            while (res.moveToNext()) {
                sorter.add(new triplicate((Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(4)));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                Reminder reminder = sorter.get(i).reminder;
                calendar.setTimeInMillis(reminder.timeUntil);
                String priority = helper.getPriority(reminder.priority);
                String msg = reminder.name + "," + dateFormat.format(calendar.getTime()) + "," + priority + ","
                        + reminder.frequency + "," + sorter.get(i).id + "," + reminder.timeUntil + "," + reminder.priority + "," + reminder.tier;
                dataList.add(msg);
            }
        }
        if (dataList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
        }
        addAll(dataList);
    }

    private void initRecyclerViewTier(String selTier) {
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<triplicate> sorter = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        if (res.getCount() == 0) {

        } else {
            while (res.moveToNext()) {
                sorter.add(new triplicate((Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(4)));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                Reminder reminder = sorter.get(i).reminder;
                if ((sorter.get(i).reminder.tier).equals(selTier.toUpperCase())) {
                    calendar.setTimeInMillis(reminder.timeUntil);
                    String priority = helper.getPriority(reminder.priority);
                    String msg = reminder.name + "," + dateFormat.format(calendar.getTime()) + "," + priority + ","
                            + reminder.frequency + "," + sorter.get(i).id + "," + reminder.timeUntil + "," + reminder.priority + "," + reminder.tier;
                    dataList.add(msg);
                }
            }
        }
        if (dataList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
        }
        addAll(dataList);
    }

    private void initRecyclerViewPriority(String selPriority) {
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<triplicate> sorter = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        if (res.getCount() == 0) {

        } else {
            while (res.moveToNext()) {
                sorter.add(new triplicate((Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(4)));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                Reminder reminder = sorter.get(i).reminder;
                if ((helper.getPriority(sorter.get(i).reminder.priority)).equals(selPriority)) {
                    calendar.setTimeInMillis(reminder.timeUntil);
                    String priority = helper.getPriority(reminder.priority);
                    String msg = reminder.name + "," + dateFormat.format(calendar.getTime()) + "," + priority + ","
                            + reminder.frequency + "," + sorter.get(i).id + "," + reminder.timeUntil + "," + reminder.priority + "," + reminder.tier;
                    dataList.add(msg);
                }
            }
        }
        if (dataList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
        }
        addAll(dataList);
    }


}
