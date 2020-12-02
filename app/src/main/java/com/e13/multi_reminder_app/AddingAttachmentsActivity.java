package com.e13.multi_reminder_app;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddingAttachmentsActivity extends AppCompatActivity {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper = new DatabaseHelper(this);
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
            setContentView(R.layout.attachments_list);

            final CheckBox checkBox = findViewById(R.id.checkBoxAttachments);
            final LinearLayout filters = findViewById(R.id.searchFilters);
            final EditText editTextName = findViewById(R.id.editTextName);
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
                        editTextName.setText("");
                        selTier = "None";
                        selPriority = "None";
                    }
                    initRecyclerView(editTextName.getText().toString(), selTier, selPriority);
                }
            });

            tiers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String clickedItem = (String) parent.getItemAtPosition(position);
                    selTier = clickedItem;
                    initRecyclerView(editTextName.getText().toString(), clickedItem, selPriority);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { selTier = "None"; } });

            priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String clickedItem = (String) parent.getItemAtPosition(position);
                    selPriority = clickedItem;
                    initRecyclerView(editTextName.getText().toString(), selTier, clickedItem);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { selPriority = "None"; } });

            editTextName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    initRecyclerView(editTextName.getText().toString(), selTier, selPriority);
                }
            });

            recyclerAdapter = new RecyclerViewAdapterAttachments(recyclerList, this);
            initRecyclerView(editTextName.getText().toString(), selTier, selPriority);
            RecyclerView recyclerView = findViewById(R.id.recycler_view_attachments);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
                sorter.add(new triplicate((Reminder) dbHelper.readByte(res.getBlob(1)), res.getInt(0), res.getInt(4)));
                sorter.sort(null);
            }
            for (int i = 0; i < sorter.size(); i++) {
                dl.add(new Pair(sorter.get(i).reminder, sorter.get(i).reminder.attachment));
            }
        }
        if (dl.size() == 0) {
            Toast.makeText(getApplicationContext(), "No reminders to show", Toast.LENGTH_LONG).show();
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
            System.out.println("called");
        for (int i = 0; i < dataList.size(); i++) {
            Pair pair = dataList.get(i);
            Reminder reminder = pair.reminder;
            System.out.println(!(reminder.tier).equals(selTier.toUpperCase()));
            if (!(reminder.tier).equals(selTier.toUpperCase())) {
                System.out.println("tierRemoved");
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

}
