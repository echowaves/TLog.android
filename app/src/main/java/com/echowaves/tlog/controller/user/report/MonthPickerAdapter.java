package com.echowaves.tlog.controller.user.report;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.YearMonth;

import java.util.ArrayList;

/**
 * Created by dmitry on 4/18/16.
 */


public class MonthPickerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String year;
    public MonthPickerAdapter(Context context, ArrayList<String> months, String year) {
        super(context, 0, months);
        this.context = context;
        this.year = year;
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

        YearMonth jodaMonth = new YearMonth(0, new Integer(month).intValue());
        monthText.setText(jodaMonth.toString("MMMM"));


        actionCodesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", "action codes for month clicked: " + month);
                String[] params = {year, month};
                TLApplicationContextProvider.getContext().setCurrentActivityObject(params);

                Intent actionCodesReport = new Intent(TLApplicationContextProvider.getContext(), ActionCodesReport.class);
                context.startActivity(actionCodesReport);

            }
        });

        employeesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", "employeees for month clicked: " + month);
                String[] params = {year, month};
                TLApplicationContextProvider.getContext().setCurrentActivityObject(params);

                Intent employeesReport = new Intent(TLApplicationContextProvider.getContext(), ActionCodesReport.class);
                context.startActivity(employeesReport);

            }
        });

        return convertView;
    }
}