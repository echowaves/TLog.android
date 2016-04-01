package com.echowaves.tlog.controller.user.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLEmployee;

import java.util.ArrayList;

/**
 * Created by dmitry on 3/31/16.
 */


public class EmployeesAdapter extends ArrayAdapter<TLEmployee> {
    public EmployeesAdapter(Context context, ArrayList<TLEmployee> employess) {
        super(context, 0, employess);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TLEmployee employee = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_employee_item_employee, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.user_employee_item_employee_name);
        TextView email = (TextView) convertView.findViewById(R.id.user_employee_item_employee_email);
        TextView sub = (TextView) convertView.findViewById(R.id.user_employee_item_employee_sub);

        // Populate the data into the template view using the data object
        name.setText(employee.getName());
        email.setText(employee.getEmail());
        if (employee.getSubcontractor().booleanValue() == false) {
            sub.setVisibility(View.INVISIBLE);
        } else {
            sub.setVisibility(View.VISIBLE);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}