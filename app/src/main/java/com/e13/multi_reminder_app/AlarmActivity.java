package com.e13.multi_reminder_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.CANADA);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);

        final TextView big = findViewById(R.id.textViewBig);
        final TextView small = findViewById(R.id.textViewSmall);
        final Button ignore = findViewById(R.id.ignoreButton);
        final Button dismiss = findViewById(R.id.dismissButton);

        String reminderName = getIntent().getStringExtra("reminderName");

        big.setText(dateFormat.format(calendar.getTime()));
        small.setText(String.format("%s is active", reminderName));

        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmActivity.this, ActiveRemindersActivity.class));
                finish();
            }
        });

    }
}
