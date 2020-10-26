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

public class main_page_activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        final Button macro = findViewById(R.id.macroButton);
        final Button meso = findViewById(R.id.mesoButton);
        final Button micro = findViewById(R.id.microButton);
        final Button newReminder = findViewById(R.id.newReminder1);

        macro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page_activity.this, macro_activity.class));
            }
        });

        meso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page_activity.this, meso_activity.class));
            }
        });

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page_activity.this, micro_activity.class));
            }
        });

        newReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_page_activity.this, reminder_creation_activity.class));
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.comingUpTabDrawer:
//                        drawerLayout.closeDrawers();
//                        startActivity(new Intent(activity_drawer.this, coming_up_activity.class));
//                        return true;
                    case R.id.newReminderDrawer:
                        startActivity(new Intent(main_page_activity.this, reminder_creation_activity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.macroButtonDrawer:
                        startActivity(new Intent(main_page_activity.this, macro_activity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.mesoButtonDrawer:
                        startActivity(new Intent(main_page_activity.this, meso_activity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.microButtonDrawer:
                        startActivity(new Intent(main_page_activity.this, micro_activity.class));
                        drawerLayout.closeDrawers();
                        return true;
//                    case R.id.settings:
//                        startActivity(new Intent(activity_drawer.this, settings.class));
//                        drawerLayout.closeDrawers();
//                        return true;
                }
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