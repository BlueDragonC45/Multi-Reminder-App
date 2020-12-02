package com.e13.multi_reminder_app;

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

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ActiveRemindersActivity extends AppCompatActivity {

    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<Pair> activeReminders = new ArrayList<>();
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
                startActivity(new Intent(ActiveRemindersActivity.this, helper.helpingNavOnClick(item)));
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

    public void addAll(ArrayList<Pair> list){
        activeReminders.clear();
        activeReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        ArrayList<Pair> dataList = new ArrayList<>();
        ArrayList<triplicate> sorter = new ArrayList<>();
        Cursor res = dbHelper.getAllData();

        if(res.getCount() == 0) {

        } else {
            while (res.moveToNext()) {
                sorter.add(new triplicate( (Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(4)));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                Reminder reminder = sorter.get(i).reminder;
                if (sorter.get(i).flag == 1) {
                    dataList.add(new Pair(reminder, sorter.get(i).id));
                }
            }
        }
        if (dataList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
        }
        addAll(dataList);
    }


}

