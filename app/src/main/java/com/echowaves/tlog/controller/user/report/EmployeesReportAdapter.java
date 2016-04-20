package com.echowaves.tlog.controller.user.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLObject;

import java.util.ArrayList;

/**
 * Created by dmitry on 4/18/16.
 */


public class EmployeesReportAdapter extends ArrayAdapter<TLObject> {
    public EmployeesReportAdapter(String year, String month, Context context, ArrayList<TLObject> employees) {
        super(context, 0, employees);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TLObject employee = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_item_employees_report, parent, false);
        }

        TextView employeeName = (TextView) convertView.findViewById(R.id.user_report_item_employees_report_nameText);
        employeeName.setText((String)employee.getValue(0));

        TextView durationText = (TextView) convertView.findViewById(R.id.user_report_item_employees_report_durationText);
        if (new Integer((String) employee.getValue(1)) != 0) {
            durationText.setText((String) employee.getValue(1) + " hours");
        } else {
            durationText.setText("--");
        }


        return convertView;
    }
}