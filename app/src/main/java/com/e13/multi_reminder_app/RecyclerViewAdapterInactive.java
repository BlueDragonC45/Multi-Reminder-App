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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.DatabaseHelper;
import com.e13.multi_reminder_app.HelperMethods;
import com.e13.multi_reminder_app.MacroActivity;
import com.e13.multi_reminder_app.NotificationHandler;
import com.e13.multi_reminder_app.R;
import com.e13.multi_reminder_app.ReminderUpdateActivity;

import java.util.ArrayList;

public class RecyclerViewAdapterInactive extends RecyclerView.Adapter<RecyclerViewAdapterInactive.ViewHolder> {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper;
    private ArrayList<String> mlist;
    private Context mContext;

    public RecyclerViewAdapterInactive(ArrayList<String> list, Context context) {
        mlist = list;
        mContext = context;
        dbHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInactive.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInactive.ViewHolder holder, final int position) {
        final String[] str = mlist.get(position).split(",");
        holder.reminderName.setText(String.format("%s: %s", str[0], str[1]));
        holder.priorityColor.setImageResource(helper.getPriorityImage(Integer.parseInt(str[6])));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(mContext);
                builder.setTitle(str[0]);
                String msg = "Date: " + str[1] + "\n" + "Priority: " + str[2] + "\n" + "Frequency: " + str[3];
                if (dbHelper.getById(Integer.parseInt(str[4])).attachment != 0) {
                    msg += "\n Attached to: " + dbHelper.getById(dbHelper.getById(Integer.parseInt(str[4])).attachment).name;
                }
                builder.setMessage(msg);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder confirm =  new AlertDialog.Builder(mContext);
                        confirm.setTitle("Are you sure?");
                        confirm.setNeutralButton("Cancel", null);
                        confirm.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteData(Integer.parseInt(str[4]));
                                Toast.makeText(mContext, "Reminder " + str[0] + " deleted", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent (v.getContext(), MacroActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                v.getContext().startActivity(intent);
                                v.getContext().startService(new Intent(v.getContext(), NotificationHandler.class));
                                Activity activity = (Activity) v.getContext();
                                activity.finish();
                            }
                        });
                        AlertDialog confirmAlert = confirm.create();
                        confirmAlert.show();
                    }
                });

                builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder update =  new AlertDialog.Builder(mContext);
                        update.setTitle("How would you like to Update?");
                        update.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent (mContext, ReminderUpdateActivity.class);
                                intent.putExtra("oldReminder", dbHelper.makeByte(dbHelper.getById(Integer.parseInt(str[4]))));
                                intent.putExtra("id", Integer.parseInt(str[4]));
                                mContext.startActivity(intent);
                                Activity activity = (Activity) v.getContext();
                                activity.finish();
                            }
                        });

                        update.setNegativeButton("Quick Delay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.cascadeDelay(Integer.parseInt(str[4]));
                                Intent intent = new Intent (mContext, mContext.getClass());
                                mContext.startActivity(intent);
                                Activity activity = (Activity) mContext;
                                activity.finish();
                            }
                        });

                        AlertDialog updateAlert = update.create();
                        updateAlert.show();
                    }
                });

                builder.setNeutralButton("Cancel", null);

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

