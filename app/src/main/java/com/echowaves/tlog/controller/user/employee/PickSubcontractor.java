package com.echowaves.tlog.controller.user.employee;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.localytics.android.Localytics;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class PickSubcontractor extends AppCompatActivity {
    private Context context;



    private Button backButton;

    private AutoCompleteTextView actionCodeTextField;

    private ListView listView;

    ArrayList<TLSubcontractor> subcontractors;

    EmployeeSubcontractorsAdapter employeeSubcontractorsAdapter;

    ArrayList<TLSubcontractor> completionsSubcontractors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("PickSubcontractor");

        setContentView(R.layout.user_employee_activity_pick_subcontractor);

        context = this;

        backButton = (Button) findViewById(R.id.user_employee_activity_pick_subcontractor_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

//        title = (TextView) findViewById(R.id.user_employee_activity_pick_subcontractor_title);
//        title.setText(employee.getName());


        listView = (ListView) findViewById(R.id.user_employee_activity_pick_subcontractor_listView);

        loadSubcontractors();

        actionCodeTextField = (AutoCompleteTextView) findViewById(R.id.user_employee_activity_pick_subcontractor_autoCompleteTextView);
        actionCodeTextField.setThreshold(1);//will start working from first character


    }


    private void loadSubcontractors() {
// Attach the adapter to a ListView

        subcontractors = new ArrayList<TLSubcontractor>();

        // Create the adapter to convert the array to views

        employeeSubcontractorsAdapter = new EmployeeSubcontractorsAdapter(this, subcontractors);

        TLSubcontractor.loadAll(
                new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {
                            JSONArray jsonSubcontractors = (JSONArray) jsonResponse.get("subcontractors");

                            for (int i = 0; i < jsonSubcontractors.length(); i++) {
                                JSONObject jsonSubcontractor = jsonSubcontractors.getJSONObject(i);
                                TLSubcontractor subcontractor =
                                        new TLSubcontractor(
                                                jsonSubcontractor.getInt("id"),
                                                jsonSubcontractor.getString("name"));

                                if (jsonSubcontractor.getString("coi_expires_at") != null && !jsonSubcontractor.getString("coi_expires_at").equals("null")) {

                                    Date coi_expires_at = new DateTime(jsonSubcontractor.getString("coi_expires_at")).toDate();
                                    subcontractor.setCoiExpiresAt(coi_expires_at);
                                }
                                subcontractors.add(subcontractor);
                            }

                            listView.setAdapter(employeeSubcontractorsAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    TLSubcontractor subcontractor = subcontractors.get(position);

                                }

                            });

                        } catch (
                                JSONException exception
                                )

                        {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseBody, Throwable error) {
                    }
                }

        );

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
