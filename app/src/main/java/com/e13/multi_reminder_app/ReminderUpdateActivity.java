package com.e13.multi_reminder_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReminderUpdateActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private String oldName;
    private long oldTimeUntil;
    private int priority = -1;
    private int oldPriority = -1;
    private String tier;
    private String oldTier;
    private String frequency;
    private String oldFrequency;
    private int oldAttachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_creation);

        final Reminder oldReminder = (Reminder) dbHelper.readByte(getIntent().getByteArrayExtra("oldReminder"));
        final int id = getIntent().getIntExtra("id", 0);
        oldName = oldReminder.name;
        oldTimeUntil = oldReminder.timeUntil;
        oldPriority = oldReminder.priority;
        oldTier = oldReminder.tier;
        oldFrequency = oldReminder.frequency;
        oldAttachment = oldReminder.attachment;

        final Button viewReminder = findViewById(R.id.nr_cancel);

        viewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button updateReminder = findViewById(R.id.nr_create);
        final EditText name = findViewById(R.id.editTextName);
        final DatePicker datePicker = findViewById(R.id.createAlarm_datePicker);
        final TimePicker timePicker = findViewById(R.id.createAlarm_timePicker);
        updateReminder.setText(R.string.update_reminder);

        initUpdate();

        updateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeUntil = helper.getTimeUntil(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        timePicker.getHour(), timePicker.getMinute());
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
                } else if (timeUntil <= 0) {
                    Toast.makeText(getApplicationContext(), "That date and time has already passed!", Toast.LENGTH_LONG).show();
                    return;
                }
                Reminder rmd = new Reminder(name.getText().toString(), timeUntil, priority, tier, frequency, oldAttachment);
                int attachment;
                if (rmd.attachment == 0) { attachment = 0; } else { attachment = 1; }
                if (dbHelper.updateData(id, rmd, attachment, dbHelper.isActive(rmd))) {
                    Toast.makeText(getApplicationContext(), "Reminder " + rmd.name + " updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Reminder not updated!", Toast.LENGTH_LONG).show();
                }
                startService(new Intent(ReminderUpdateActivity.this, NotificationHandler.class));
                finish();
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
                frequency = "Never";
                break;
            case R.id.radioHour:
                frequency = "Hourly";
                break;
            case R.id.radioDay:
                frequency = "Daily";
                break;
            case R.id.radioWeek:
                frequency = "Weekly";
                break;
            case R.id.radioMonth:
                frequency = "Monthly";
                break;
        }
    }

    public void initUpdate() {
        EditText updName = findViewById(R.id.editTextName);
        DatePicker updDatePicker = findViewById(R.id.createAlarm_datePicker);
        TimePicker updTimePicker = findViewById(R.id.createAlarm_timePicker);
        updName.setText(oldName);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(oldTimeUntil);
        updDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        updTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        updTimePicker.setMinute(calendar.get(Calendar.MINUTE));
        updateRadioButtonsInt(oldPriority);
        updateRadioButtons(oldTier);
        updateRadioButtons(oldFrequency);
        priority = oldPriority;
        tier = oldTier;
        frequency = oldFrequency;
    }

    private void updateRadioButtonsInt(int num) {
        final RadioButton rUrgent = findViewById(R.id.radioUrgent);
        final RadioButton rHigh = findViewById(R.id.radioHigh);
        final RadioButton rMedium = findViewById(R.id.radioMed);
        final RadioButton rLow = findViewById(R.id.radioLow);
        switch (num) {
            case 4:
                rUrgent.toggle();
                break;
            case 3:
                rHigh.toggle();
                break;
            case 2:
                rMedium.toggle();
                break;
            case 1:
                rLow.toggle();
                break;
        }
    }

    private void updateRadioButtons(String str) {
        final RadioButton rMacro = findViewById(R.id.radioMacro);
        final RadioButton rMeso = findViewById(R.id.radioMeso);
        final RadioButton rMicro = findViewById(R.id.radioMicro);
        final RadioButton rNever = findViewById(R.id.radioNever);
        final RadioButton rHourly = findViewById(R.id.radioHour);
        final RadioButton rDaily = findViewById(R.id.radioDay);
        final RadioButton rWeekly = findViewById(R.id.radioWeek);
        final RadioButton rMonthly = findViewById(R.id.radioMonth);
        switch (str) {
            case "MACRO":
                rMacro.toggle();
                break;
            case "MESO":
                rMeso.toggle();
                break;
            case "MICRO":
                rMicro.toggle();
                break;
            case "Never":
                rNever.toggle();
                break;
            case "Hourly":
                rHourly.toggle();
                break;
            case "Daily":
                rDaily.toggle();
                break;
            case "Weekly":
                rWeekly.toggle();
                break;
            case "Monthly":
                rMonthly.toggle();
                break;
        }
    }

}
