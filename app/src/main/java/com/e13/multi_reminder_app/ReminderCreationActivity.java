package com.e13.multi_reminder_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderCreationActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private int priority = -1;
    private String tier;
    private String frequency;
    private int attachment = 0;
    private int attachmentFlag = 0;
    static TextView currentAttachment;
    static NestedScrollView attachmentsLayout;

    ArrayList<Pair> dataList = new ArrayList<>();
    ArrayList<String> tiersList = new ArrayList<>();
    ArrayList<String> prioritiesList = new ArrayList<>();
    ArrayList<Pair> recyclerList = new ArrayList<>();
    RecyclerViewAdapterAttachments recyclerAdapter;
    String selTier = "None";
    String selPriority = "None";

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

        currentAttachment = findViewById(R.id.currentAttachment);

        final Button createReminder = findViewById(R.id.nr_create);
        final EditText name = findViewById(R.id.editTextName);
        final DatePicker datePicker = findViewById(R.id.createAlarm_datePicker);
        final TimePicker timePicker = findViewById(R.id.createAlarm_timePicker);

        createReminder.setOnClickListener(new View.OnClickListener() {
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
                if (!currentAttachment.getText().toString().isEmpty()) {
                    attachment = Integer.parseInt(currentAttachment.getText().toString());
                    attachmentFlag = 1; }
                Reminder rmd = new Reminder(name.getText().toString(), timeUntil, priority, tier, frequency, attachment);
                if (dbHelper.insertData(rmd, attachmentFlag)) {
                    Toast.makeText(getApplicationContext(), "Reminder " + rmd.name + " added!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Reminder not added!", Toast.LENGTH_LONG).show();
                }
                startService(new Intent(ReminderCreationActivity.this, NotificationHandler.class));
                finish();
            }
        });

//-----------------------------------------------------------------------------------------------
        final CheckBox checkBox = findViewById(R.id.checkBoxAttachments);
        final LinearLayout filters = findViewById(R.id.searchFilters);
        final EditText editTextSearchName = findViewById(R.id.editTextSearchName);
        final Spinner tiers = findViewById(R.id.spinnerTier);
        final Spinner priority = findViewById(R.id.spinnerPriority);

        initLists();
        mSpinnerAdapter AdapterTiers = new mSpinnerAdapter(this, tiersList);
        mSpinnerAdapter AdapterPriorities = new mSpinnerAdapter(this, prioritiesList);
        tiers.setAdapter(AdapterTiers);
        priority.setAdapter(AdapterPriorities);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    filters.setVisibility(View.VISIBLE);
                } else {
                    filters.setVisibility(View.GONE);
                    editTextSearchName.setText("");
                    selTier = "None";
                    selPriority = "None";
                }
                initRecyclerView(editTextSearchName.getText().toString(), selTier, selPriority);
            }
        });

        tiers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) parent.getItemAtPosition(position);
                selTier = clickedItem;
                initRecyclerView(editTextSearchName.getText().toString(), clickedItem, selPriority);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { selTier = "None"; } });

        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) parent.getItemAtPosition(position);
                selPriority = clickedItem;
                initRecyclerView(editTextSearchName.getText().toString(), selTier, clickedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { selPriority = "None"; } });

        editTextSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                initRecyclerView(editTextSearchName.getText().toString(), selTier, selPriority);
            }
        });

        recyclerAdapter = new RecyclerViewAdapterAttachments(recyclerList, this);
        initRecyclerView(editTextSearchName.getText().toString(), selTier, selPriority);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_attachments);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Button addAttachments = findViewById(R.id.addAttachmentButton);
        attachmentsLayout = findViewById(R.id.nScrollView);
        attachmentsLayout.setVisibility(View.GONE);

        addAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachmentsLayout.getVisibility() == View.GONE) {
                    attachmentsLayout.setVisibility(View.VISIBLE);
                } else {
                    attachmentsLayout.setVisibility(View.GONE);
                    editTextSearchName.setText("");
                    selTier = "None";
                    selPriority = "None";
                }
                initRecyclerView(editTextSearchName.getText().toString(), selTier, selPriority);
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

    private void initLists() {
        tiersList.add("None");
        tiersList.add("Macro");
        tiersList.add("Meso");
        tiersList.add("Micro");
        prioritiesList.add("None");
        prioritiesList.add("Urgent");
        prioritiesList.add("High");
        prioritiesList.add("Medium");
        prioritiesList.add("Low");
    }

    public void addAll(ArrayList<Pair> list){
        recyclerList.clear();
        recyclerList.addAll(list);
        recyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(String name, String selTier, String selPriority) {
        dataList.clear();
        dataList.addAll(initRecyclerViewAll());
        if (name != null && !name.isEmpty()) {
            System.out.println("name");
            removeNames(name);
        }
        if (!selTier.equals("None")) {
            removeTier(selTier);
        }
        if (!selPriority.equals("None")) {
            System.out.println("priority");
            removePriority(selPriority);
        }
    }

    private ArrayList<Pair> initRecyclerViewAll() {
        ArrayList<Pair> dl = new ArrayList<>();
        ArrayList<triplicate> sorter = new ArrayList<>();
        Cursor res = dbHelper.getAllData();

        if (res.getCount() == 0) {

        } else {
            while (res.moveToNext()) {
                sorter.add(new triplicate((Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(2)));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                if ((sorter.get(i).flag) == 0) {
                    dl.add(new Pair(sorter.get(i).reminder, sorter.get(i).id));
                }
            }
        }
        addAll(dl);
        return dl;
    }

    private void removeNames(String name) {
        for (int i = 0; i < dataList.size(); i++) {
            Pair pair = dataList.get(i);
            Reminder reminder = pair.reminder;
            Pattern p = Pattern.compile(name);
            Matcher m = p.matcher(reminder.name.toLowerCase());
            if (!m.find()) {
                dataList.remove(pair);
                i--;
            }
        }
        addAll(dataList);
    }

    private void removeTier( String selTier) {
        for (int i = 0; i < dataList.size(); i++) {
            Pair pair = dataList.get(i);
            Reminder reminder = pair.reminder;
            if (!(reminder.tier).equals(selTier.toUpperCase())) {
                dataList.remove(pair);
                i--;
            }
        }
        addAll(dataList);
    }

    private void removePriority(String selPriority) {
        for (int i = 0; i < dataList.size(); i++) {
            Pair pair = dataList.get(i);
            Reminder reminder = pair.reminder;
            if (!(helper.getPriority(reminder.priority)).equals(selPriority)) {
                dataList.remove(pair);
                i--;
            }
        }
        addAll(dataList);
    }

    public static void changeAttachment(int id) {
        attachmentsLayout.setVisibility(View.GONE);
        currentAttachment.setText(String.format("%s", id));
        System.out.println(currentAttachment.getText().toString());
    }

}