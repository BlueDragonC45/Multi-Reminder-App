package com.e13.multi_reminder_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class MainPageActivity extends AppCompatActivity {

    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        startService(new Intent(MainPageActivity.this, NotificationHandler.class));

        final Button active = findViewById(R.id.activeButton);
        final Button comingUp = findViewById(R.id.comingUpButton);
        final Button macro = findViewById(R.id.macroButton);
        final Button meso = findViewById(R.id.mesoButton);
        final Button micro = findViewById(R.id.microButton);
        final Button newReminder = findViewById(R.id.newReminder1);

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, ActiveRemindersActivity.class));
            }
        });

        comingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, comingUpActivity.class));
            }
        });

        macro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, MacroActivity.class));
            }
        });

        meso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, MesoActivity.class));
            }
        });

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, MicroActivity.class));
            }
        });

        newReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, ReminderCreationActivity.class));
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
                startActivity(new Intent(MainPageActivity.this, helper.helpingNavOnClick(item)));
                return true;
            }
        });

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



}