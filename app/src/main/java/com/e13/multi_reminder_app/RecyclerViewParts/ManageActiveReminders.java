package com.e13.multi_reminder_app.RecyclerViewParts;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.R;

import java.util.ArrayList;
public class ManageActiveReminders extends AppCompatActivity {

    private ArrayList<String> activeReminders = new ArrayList<>();
    RecyclerViewAdapterActive adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_reminders);

        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_active);
        adapter = new RecyclerViewAdapterActive(activeReminders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void addAll(ArrayList<String> list){
        activeReminders.clear();
        activeReminders.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initList() {

    }


}
