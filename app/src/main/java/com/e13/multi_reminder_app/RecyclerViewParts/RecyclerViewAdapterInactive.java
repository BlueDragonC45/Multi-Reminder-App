package com.e13.multi_reminder_app.RecyclerViewParts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e13.multi_reminder_app.R;

import java.util.ArrayList;

public class RecyclerViewAdapterInactive extends RecyclerView.Adapter<RecyclerViewAdapterInactive.ViewHolder> {

    private ArrayList<String> mlist;
    private Context mContext;

    public RecyclerViewAdapterInactive(ArrayList<String> list, Context context) {
        mlist = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInactive.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInactive.ViewHolder holder, final int position) {
        holder.reminderName.setText(mlist.get(position));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mlist.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView reminderName;
        LinearLayout parent_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.text_value);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

}

