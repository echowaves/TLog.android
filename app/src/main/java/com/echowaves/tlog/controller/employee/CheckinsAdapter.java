package com.echowaves.tlog.controller.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.model.TLCheckin;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by dmitry on 4/15/16.
 */


public class CheckinsAdapter extends ArrayAdapter<TLCheckin> {
    public CheckinsAdapter(Context context, ArrayList<TLCheckin> checkins) {
        super(context, 0, checkins);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TLCheckin checkin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_item_checkin, parent, false);
        }
        // Lookup view for data population
        TextView checkedInAt = (TextView) convertView.findViewById(R.id.employee_item_checkin_checkedInAtLabel);
        TextView duration = (TextView) convertView.findViewById(R.id.employee_item_checkin_durationLabel);
        TextView actionCode = (TextView) convertView.findViewById(R.id.employee_item_checkin_actionCodeLabel);

        // Populate the data into the template view using the data object
        checkedInAt.setText(new DateTime(checkin.getCheckedInAt()).toString(TLConstants.defaultDateFormat));
        duration.setText(checkin.getDurationText());
        actionCode.setText(checkin.getActionCode().getCode() + ":" + checkin.getActionCode().getDescr());

        // Return the completed view to render on screen
        return convertView;
    }
}

