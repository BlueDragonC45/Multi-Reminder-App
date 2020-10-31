package com.e13.multi_reminder_app.RecyclerViewParts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.DatabaseHelper;
import com.e13.multi_reminder_app.HelperMethods;
import com.e13.multi_reminder_app.MacroActivity;
import com.e13.multi_reminder_app.R;
import com.e13.multi_reminder_app.Reminder;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ManageActiveReminders extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<String> activeReminders = new ArrayList<>();
    RecyclerViewAdapterActive adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_list);

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
                startActivity(new Intent(ManageActiveReminders.this, helper.helpingNavOnClick(item)));
                return true;
            }
        });

        adapter = new RecyclerViewAdapterActive(activeReminders, this);
        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_active);
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
        activeReminders.clear();
        activeReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        ArrayList<String> dataList = new ArrayList<>();
        Cursor res = dbHelper.getAllData();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

        if(res.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No active reminders to show", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
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
