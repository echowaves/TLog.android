package com.echowaves.tlog.controller.user.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLActionCode;
import com.echowaves.tlog.model.TLSubcontractor;

import java.util.ArrayList;

/**
 * Created by dmitry on 3/31/16.
 */


public class EmployeeSubcontractorsAdapter extends ArrayAdapter<TLSubcontractor> {
    public EmployeeSubcontractorsAdapter(Context context, ArrayList<TLSubcontractor> subcontractors) {
        super(context, 0, subcontractors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TLSubcontractor subcontractor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_employee_item_subcontractor, parent, false);
        }
        // Lookup view for data population
        TextView descr = (TextView) convertView.findViewById(R.id.user_employee_item_subcontractor_descr);

        // Populate the data into the template view using the data object
        descr.setText(subcontractor.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}