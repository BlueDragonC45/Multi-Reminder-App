package com.e13.multi_reminder_app.RecyclerViewParts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.DatabaseHelper;
import com.e13.multi_reminder_app.HelperMethods;
import com.e13.multi_reminder_app.R;
import com.e13.multi_reminder_app.Settings;

import java.util.ArrayList;

public class RecyclerViewAdapterAttachments extends RecyclerView.Adapter<RecyclerViewAdapterAttachments.ViewHolder> {

    HelperMethods helper = new HelperMethods();
    DatabaseHelper dbHelper;
    Settings settings = new Settings();
    private ArrayList<String> mlist;
    private Context mContext;

    public RecyclerViewAdapterAttachments(ArrayList<String> mlist, Context mcontext) {
        this.mlist = mlist;
        this.mContext = mcontext;
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
        final String[] str = mlist.get(position).split(",");
        holder.reminderName.setText(String.format("%s:    %s", str[0], str[1]));
        holder.priorityColor.setImageResource(helper.getPriorityImage(Integer.parseInt(str[6])));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //NEED THIS
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


