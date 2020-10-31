package com.e13.multi_reminder_app.RecyclerViewParts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.DatabaseHelper;
import com.e13.multi_reminder_app.HelperMethods;
import com.e13.multi_reminder_app.NotificationHandler;
import com.e13.multi_reminder_app.R;
import com.e13.multi_reminder_app.Reminder;

import java.util.ArrayList;

public class RecyclerViewAdapterActive extends RecyclerView.Adapter<RecyclerViewAdapterActive.ViewHolder> {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper;
    private ArrayList<String> mlist;
    private Context mContext;

    public RecyclerViewAdapterActive (ArrayList<String> mlist, Context mcontext) {
        this.mlist = mlist;
        this.mContext = mcontext;
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
        final String[] str = mlist.get(position).split(",");
        holder.reminderName.setText(String.format("%s:    %s", str[0], str[1]));
        holder.priorityColor.setImageResource(helper.getPriorityImage(Integer.parseInt(str[6])));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder =  new AlertDialog.Builder(mContext);
                builder.setTitle(str[0]);
                builder.setMessage("Date: " + str[1] + "\n" + "Priority: " + str[2] + "\n" + "Frequency: " + str[3]);

                builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (str[3].equals("Never")) {
                            dbHelper.deleteData(Integer.parseInt(str[4]));
                        } else {
                            long newTime = helper.findNewTime(str[3], Long.parseLong(str[5]));
                            dbHelper.updateData(Integer.parseInt(str[4]), new Reminder(str[0], newTime, Integer.parseInt(str[6]), str[7], str[3]),
                                    0, 0);
                        }
                        Intent intent = new Intent (v.getContext(), NotificationHandler.class);
                        v.getContext().startActivity(intent);
                        Activity activity = (Activity) v.getContext();
                        activity.finish();

                    }
                });

                builder.setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.updateData(Integer.parseInt(str[4]), new Reminder(str[0], Long.parseLong(str[5]) + 300000, Integer.parseInt(str[6]), str[7], str[3]),
                                0, 0);
                        Intent intent = new Intent (v.getContext(), NotificationHandler.class);
                        v.getContext().startActivity(intent);
                        Activity activity = (Activity) v.getContext();
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

