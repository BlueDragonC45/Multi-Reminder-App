package com.e13.multi_reminder_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.e13.multi_reminder_app.R;

import java.util.ArrayList;

public class mSpinnerAdapter extends ArrayAdapter<String> {

    public mSpinnerAdapter(Context context, ArrayList<String> spinnerEntries) {
        super(context, 0, spinnerEntries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_layout, parent, false);
        }
        TextView service = convertView.findViewById(R.id.spinner_text);

        String currentItem = (String) getItem(position);

        assert currentItem != null;
        service.setText(currentItem);

        return convertView;
    }

}
