package com.echowaves.tlog.controller.user.report;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.echowaves.tlog.R;

import java.util.ArrayList;

/**
 * Created by dmitry on 4/18/16.
 */


public class MonthPickerAdapter extends ArrayAdapter<String> {
    public MonthPickerAdapter(Context context, ArrayList<String> months) {
        super(context, 0, months);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final String month = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_item_month_picker, parent, false);
        }

        TextView monthText = (TextView) convertView.findViewById(R.id.user_report_item_month_picker_monthText);
        // Lookup view for data population
        Button actionCodesButton = (Button) convertView.findViewById(R.id.user_report_item_month_picker_actionCodesButton);
        Button employeesButton = (Button) convertView.findViewById(R.id.user_report_item_month_picker_employeesButton);


        // Populate the data into the template view using the data object
        monthText.setText(month);

        actionCodesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", "action codes for month clicked: " + month);


            }
        });

        employeesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", "employeees for month clicked: " + month);


            }
        });

        return convertView;
    }
}