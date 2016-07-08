package com.echowaves.tlog.controller.user.subcontractor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dmitry on 6/26/16.
 */


public class SubcontractorsAdapter extends ArrayAdapter<TLSubcontractor> {
    private ArrayList<TLSubcontractor> subcontractors;

    public SubcontractorsAdapter(Context context, ArrayList<TLSubcontractor> subcontractors) {
        super(context, 0, subcontractors);
        this.subcontractors = subcontractors;
    }


    @Override
    public int getViewTypeCount() {
        return subcontractors.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }




    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        final TLSubcontractor subcontractor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user_subcontractor_item_subcontractor, parent, false);
        }
        // Lookup view for data population
        final TextView name = (TextView) view.findViewById(R.id.user_subcontractor_item_subcontractor_name);
        final TextView coiExpirationDate = (TextView) view.findViewById(R.id.user_subcontractor_item_subcontractor_coi_expiration_date);

        // Populate the data into the template view using the data object
        name.setText(subcontractor.getName());



        final View subView = view;

        subcontractor.hasCOI(
                new TLJsonHttpResponseHandler(view.getContext()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        if (subcontractor.getCoiExpiresAt() != null && subcontractor.getCoiExpiresAt().getTime() > new Date().getTime()) {
                            coiExpirationDate.setText(new DateTime(subcontractor.getCoiExpiresAt()).toString(TLConstants.shortDateFormat));
                            subView.setBackgroundColor(Color.WHITE);
                            coiExpirationDate.setTextColor(Color.parseColor("#666666"));
                        } else {
                            coiExpirationDate.setText("COI expiration date is invalid");
                            subView.setBackgroundColor(Color.RED);
                            coiExpirationDate.setTextColor(Color.BLACK);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        coiExpirationDate.setText("COI is missing");
                        subView.setBackgroundColor(Color.RED);
                        coiExpirationDate.setTextColor(Color.BLACK);
                    }
                }

        );
        // Return the completed view to render on screen
        return view;
    }


}