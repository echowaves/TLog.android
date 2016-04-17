package com.echowaves.tlog.controller.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLActionCode;

import java.util.ArrayList;

/**
 * Created by dmitry on 4/16/16.
 */


public class PickActionCodeAdapter extends ArrayAdapter<TLActionCode> {
    public PickActionCodeAdapter(Context context, ArrayList<TLActionCode> actionCodes) {
        super(context, 0, actionCodes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TLActionCode actionCode = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_item_pick_action_code, parent, false);
        }
        // Lookup view for data population
        TextView text = (TextView) convertView.findViewById(R.id.employee_item_pick_action_code_text);

        // Populate the data into the template view using the data object
        text.setText(actionCode.getCode() + ":" + actionCode.getDescr());
        // Return the completed view to render on screen
        return convertView;
    }
}