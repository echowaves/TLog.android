package com.echowaves.tlog.controller.user.report;

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
 * Created by dmitry on 4/18/16.
 */


public class ActionCodesReportAdapter extends ArrayAdapter<TLActionCode> {
    public ActionCodesReportAdapter(String year, String month, Context context, ArrayList<TLActionCode> actionCodes) {
        super(context, 0, actionCodes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TLActionCode actionCode = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_item_action_codes_report, parent, false);
        }

        TextView actionCodeText = (TextView) convertView.findViewById(R.id.user_report_item_action_codes_report_nameText);
        actionCodeText.setText(actionCode.getCode() + ":" + actionCode.getDescr());

        TextView durationText = (TextView) convertView.findViewById(R.id.user_report_item_action_codes_report_durationText);
        if (new Integer((String) actionCode.getValue(0)) != 0) {
            durationText.setText((String) actionCode.getValue(0) + " hours");
        } else {
            durationText.setText("--");
        }


        return convertView;
    }
}