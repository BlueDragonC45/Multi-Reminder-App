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

import com.e13.multi_reminder_app.RecyclerViewParts.RecyclerViewAdapterInactive;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MicroActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<String> microReminders = new ArrayList<>();
    RecyclerViewAdapterInactive adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.micro_list);

        final Button newReminder =  findViewById(R.id.newReminder4);

        newReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MicroActivity.this, ReminderCreationActivity.class));
            }
        });

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        final HelperMethods helper = new HelperMethods();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MicroActivity.this, helper.helpingNavOnClick(item)));
                return true;
            }
        });

        adapter = new RecyclerViewAdapterInactive(microReminders, this);
        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_micro);
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
        microReminders.clear();
        microReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        ArrayList<String> dataList = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        if(res.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                Reminder reminder = (Reminder) dbHelper.readByte(res.getBlob(1));
                if (reminder.tier.equals("MICRO")) {
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
