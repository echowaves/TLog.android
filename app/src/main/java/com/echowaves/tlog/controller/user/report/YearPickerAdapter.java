package com.echowaves.tlog.controller.user.report;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;

import java.util.ArrayList;

/**
 * Created by dmitry on 4/18/16.
 */


public class YearPickerAdapter extends ArrayAdapter<String> {
    private Context context;

    public YearPickerAdapter(Context context, ArrayList<String> years) {
        super(context, 0, years);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final String year = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_item_year_picker, parent, false);
        }
        // Lookup view for data population
        Button yearButton = (Button) convertView.findViewById(R.id.user_report_item_year_picker_yearButton);

        // Populate the data into the template view using the data object
        yearButton.setText(year);

        yearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$",  "year clicked: " + year);

                TLApplicationContextProvider.getContext().setCurrentActivityObject(year);

                Intent monthPicker = new Intent(TLApplicationContextProvider.getContext(), MonthPicker.class);
                context.startActivity(monthPicker);


            }
        });

        return convertView;
    }
}