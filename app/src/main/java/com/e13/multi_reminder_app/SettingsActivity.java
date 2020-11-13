package com.e13.multi_reminder_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity {

    Settings settings = new Settings();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        final EditText snoozeTime = findViewById(R.id.editTextSnoozeTime);
        if (settings.getSnoozeTime() != 300000) {
            int timeMin = (int) (settings.getSnoozeTime() / 60000);
            snoozeTime.setText(String.valueOf(timeMin));
        }

        final EditText activeDelay = findViewById(R.id.editTextActiveDelay);
        if (settings.getActiveDelay() != 60000) {
            int timeMin = (int) (settings.getActiveDelay() / 60000);
            activeDelay.setText(String.valueOf(timeMin));
        }

        Button apply = findViewById(R.id.applyChangesSettings);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snoozeTime.length() != 0) {
                    long time = Long.parseLong(snoozeTime.getText().toString());
                    settings.setSnoozeTime(time);
                }
                if (activeDelay.length() != 0) {
                    long time = Long.parseLong(activeDelay.getText().toString());
                    settings.setActiveDelay(time);
                }
                Toast.makeText(getApplicationContext(), "Settings updated!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        Button cancel = findViewById(R.id.cancelSettings);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                startActivity(new Intent(SettingsActivity.this, helper.helpingNavOnClick(item)));
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
