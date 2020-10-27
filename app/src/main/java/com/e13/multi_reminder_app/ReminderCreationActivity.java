package com.e13.multi_reminder_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderCreationActivity extends AppCompatActivity {

    private int priority = -1;
    private String tier;
    private String frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_creation);

        final Button viewReminder = findViewById(R.id.nr_cancel);

        viewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button createReminder = findViewById(R.id.nr_create);
        final TextView name = findViewById(R.id.editTextName);
        final DatePicker datePicker = findViewById(R.id.createAlarm_datePicker);
        final TimePicker timePicker = findViewById(R.id.createAlarm_timePicker);

        createReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "You need to pick a name!", Toast.LENGTH_LONG).show();
                    return;
                } else if (priority == -1) {
                    Toast.makeText(getApplicationContext(), "You need to pick a priority!", Toast.LENGTH_LONG).show();
                    return;
                } else if (tier == null) {
                    Toast.makeText(getApplicationContext(), "You need to pick a tier!", Toast.LENGTH_LONG).show();
                    return;
                } else if (frequency == null) {
                    frequency = "NEVER";
                }
                Reminder rmd = new Reminder(name.toString(), datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        timePicker.getHour(), timePicker.getMinute(), priority, tier, frequency);
            }
        });

    }

    public void onClickRadioButton(View view) {
        int pressId = view.getId();

        switch (pressId) {
            case R.id.radioUrgent:
                priority = 4;
                break;
            case R.id.radioHigh:
                priority = 3;
                break;
            case R.id.radioMed:
                priority = 2;
                break;
            case R.id.radioLow:
                priority = 1;
                break;
            case R.id.radioMacro:
                tier = "MACRO";
                break;
            case R.id.radioMeso:
                tier = "MESO";
                break;
            case R.id.radioMicro:
                tier = "MICRO";
                break;
            case R.id.radioNever:
                frequency = "NEVER";
                break;
            case R.id.radioHour:
                frequency = "HOUR";
                break;
            case R.id.radioDay:
                frequency = "DAY";
                break;
            case R.id.radioWeek:
                frequency = "WEEK";
                break;
            case R.id.radioMonth:
                frequency = "MONTH";
                break;
        }
    }

}