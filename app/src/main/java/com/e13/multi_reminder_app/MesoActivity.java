package com.e13.multi_reminder_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MesoActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<String> mesoReminders = new ArrayList<>();
    RecyclerViewAdapterInactive adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meso_list);

        final Button newReminder =  findViewById(R.id.newReminder3);

        newReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MesoActivity.this, ReminderCreationActivity.class));
            }
        });


        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Simple Reminder");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        final HelperMethods helper = new HelperMethods();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MesoActivity.this, helper.helpingNavOnClick(item)));
                return true;
            }
        });

        adapter = new RecyclerViewAdapterInactive(mesoReminders, this);
        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_meso);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addAll(ArrayList<String> list){
        mesoReminders.clear();
        mesoReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<triplicate> sorter = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        if(res.getCount() == 0) {

        } else {
            while (res.moveToNext()) {
                sorter.add(new triplicate( (Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), -1));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                Reminder reminder = sorter.get(i).reminder;
                if (sorter.get(i).reminder.tier.equals("MESO")) {
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
