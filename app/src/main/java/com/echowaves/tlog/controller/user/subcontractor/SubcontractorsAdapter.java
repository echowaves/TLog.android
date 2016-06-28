package com.echowaves.tlog.controller.user.subcontractor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.model.TLSubcontractor;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by dmitry on 6/26/16.
 */


public class SubcontractorsAdapter extends ArrayAdapter<TLSubcontractor> {
    public SubcontractorsAdapter(Context context, ArrayList<TLSubcontractor> subcontractors) {
        super(context, 0, subcontractors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TLSubcontractor subcontractor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_subcontractor_item_subcontractor, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.user_subcontractor_item_subcontractor_name);
        TextView coiExpirationDate = (TextView) convertView.findViewById(R.id.user_subcontractor_item_subcontractor_coi_expiration_date);

        // Populate the data into the template view using the data object
        name.setText(subcontractor.getName());
        coiExpirationDate.setText(new DateTime(subcontractor.getCoiExpiresAt()).toString(TLConstants.shortDateFormat));
//        if (subcontractor.getSubcontractorId() == null) {
//            sub.setVisibility(View.INVISIBLE);
//        } else {
//            sub.setVisibility(View.VISIBLE);
//        }
        // Return the completed view to render on screen
        return convertView;
    }
}