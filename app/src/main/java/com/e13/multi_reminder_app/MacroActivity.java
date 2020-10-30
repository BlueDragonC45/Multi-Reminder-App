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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.e13.multi_reminder_app.RecyclerViewParts.RecyclerViewAdapterInactive;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MacroActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<String> macroReminders = new ArrayList<>();
    RecyclerViewAdapterInactive adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_reminders);

//        final Button newReminder =  findViewById(R.id.newReminder2);
//
//        newReminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MacroActivity.this, ReminderCreationActivity.class));
//            }
//        });

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
                startActivity(new Intent(MacroActivity.this, helper.helpingNavOnClick(item)));
                return true;
            }
        });

        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_macro);
        adapter = new RecyclerViewAdapterInactive(macroReminders, this);
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
        macroReminders.clear();
        macroReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        ArrayList<String> dataList = new ArrayList<>();



        addAll(dataList);
    }

}
