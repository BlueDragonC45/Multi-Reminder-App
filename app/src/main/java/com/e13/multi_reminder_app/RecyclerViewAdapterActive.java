package com.e13.multi_reminder_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class RecyclerViewAdapterActive extends RecyclerView.Adapter<RecyclerViewAdapterActive.ViewHolder> {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper;
    Settings settings = new Settings();
    private ArrayList<Pair> mlist;
    private Context mContext;
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.CANADA);

    public RecyclerViewAdapterActive (ArrayList<Pair> mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
        dbHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterActive.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterActive.ViewHolder holder, final int position) {
        final Reminder reminder = mlist.get(position).reminder;
        final int id = mlist.get(position).data;
        calendar.setTimeInMillis(reminder.timeUntil);
        holder.reminderName.setText(String.format("%s:    %s", reminder.name, dateFormat.format(calendar.getTime())));
        holder.priorityColor.setImageResource(helper.getPriorityImage(reminder.priority));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder =  new AlertDialog.Builder(mContext);
                builder.setTitle(reminder.name);
                builder.setMessage("Date: " + dateFormat.format(calendar.getTime()) + "\n" + "Priority: " + helper.getPriority(reminder.priority) + "\n" + "Frequency: " + reminder.frequency);

                builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (reminder.frequency.equals("Never")) {
                            dbHelper.deleteData(id);
                        } else {
                            long newTime = helper.findNewTime(reminder.frequency, reminder.timeUntil);
                            int attachment;
                            if (reminder.attachment == 0) { attachment = 0; } else { attachment = 1; }
                            dbHelper.updateData(id, new Reminder(reminder, newTime),
                                    attachment, 0);
                        }
                        Intent intent = new Intent (mContext, ActiveRemindersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Activity activity = (Activity) v.getContext();
                        mContext.startActivity(intent);
                        mContext.startService(new Intent(mContext, NotificationHandler.class));
                        activity.finish();

                    }
                });

                builder.setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int attachment;
                        if (reminder.attachment == 0) { attachment = 0; } else { attachment = 1; }
                        dbHelper.updateData(id, new Reminder(reminder, reminder.timeUntil + settings.getSnoozeTime()),
                                attachment, 0);
                        Intent intent = new Intent (mContext, ActiveRemindersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Activity activity = (Activity) v.getContext();
                        mContext.startActivity(intent);
                        mContext.startService(new Intent(mContext, NotificationHandler.class));
                        activity.finish();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
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

