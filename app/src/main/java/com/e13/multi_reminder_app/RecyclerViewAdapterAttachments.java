package com.e13.multi_reminder_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerViewAdapterAttachments extends RecyclerView.Adapter<RecyclerViewAdapterAttachments.ViewHolder> {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper;
    private ArrayList<pair> mList;
    private Context mContext;
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

    public RecyclerViewAdapterAttachments(ArrayList<pair> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        dbHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterAttachments.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new RecyclerViewAdapterAttachments.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterAttachments.ViewHolder holder, final int position) {
        final Reminder reminder = mList.get(position).reminder;
        calendar.setTimeInMillis(reminder.timeUntil);
        holder.reminderName.setText(String.format("%s:    %s", reminder.name, dateFormat.format(calendar.getTime())));
        holder.priorityColor.setImageResource(helper.getPriorityImage(reminder.priority));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.GONE);
                ReminderCreationActivity.changeAttachment(mList.get(position).attachment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView reminderName;
        ImageView priorityColor;
        LinearLayout parent_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.text_value);
            priorityColor = itemView.findViewById(R.id.priorityImageView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

}


