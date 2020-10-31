package com.e13.multi_reminder_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.e13.multi_reminder_app.RecyclerViewParts.ManageActiveReminders;
import com.google.android.material.navigation.NavigationView;

public class MainPageActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        final Button active = findViewById(R.id.activeButton);
        final Button macro = findViewById(R.id.macroButton);
        final Button meso = findViewById(R.id.mesoButton);
        final Button micro = findViewById(R.id.microButton);
        final Button newReminder = findViewById(R.id.newReminder1);
        final Button temp = findViewById(R.id.viewAll);
        final Button temp2 = findViewById(R.id.delete);

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, ManageActiveReminders.class));
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

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = dbHelper.getAllData();
                if(res.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Nothing to show!", Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuilder buffer = new StringBuilder();
                while (res.moveToNext()) {
                    buffer.append("Id: " + res.getInt(0) + "\n");
                    buffer.append("Reminder: " + (dbHelper.readByte(res.getBlob(1))).toString() + "\n");
                    buffer.append("This week: " + res.getInt(3) + "\n");
                    buffer.append("Active: " + res.getInt(4) + "\n");
                }
                showMessage("Reminders", buffer.toString());
            }
        });

        temp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    try {
                        dbHelper.deleteData(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}