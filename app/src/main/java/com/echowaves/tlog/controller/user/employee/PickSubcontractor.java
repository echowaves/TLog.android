package com.echowaves.tlog.controller.user.employee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.user.subcontractor.SubcontractorCreate;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.echowaves.tlog.util.TLUtil;
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
    private TLEmployee employee;


    private Button backButton;
    private Button pickButton;
    private Button createButton;

    private AutoCompleteTextView subcontractorTextField;

    private ListView listView;

    ArrayList<TLSubcontractor> subcontractors;
    private TLSubcontractor selectedSubcontractor;

    EmployeeSubcontractorsAdapter employeeSubcontractorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("PickSubcontractor");

        setContentView(R.layout.user_employee_activity_pick_subcontractor);

        context = this;

        employee = (TLEmployee) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        backButton = (Button) findViewById(R.id.user_employee_activity_pick_subcontractor_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        pickButton = (Button) findViewById(R.id.user_employee_activity_pick_subcontractor_pickButton);

        createButton = (Button) findViewById(R.id.user_employee_activity_pick_subcontractor_createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent subcontractorCreate = new Intent(TLApplicationContextProvider.getContext(), SubcontractorCreate.class);
                startActivity(subcontractorCreate);
            }
        });

//        title = (TextView) findViewById(R.id.user_employee_activity_pick_subcontractor_title);
//        title.setText(employee.getName());


        listView = (ListView) findViewById(R.id.user_employee_activity_pick_subcontractor_listView);

        loadSubcontractors();

        subcontractorTextField = (AutoCompleteTextView) findViewById(R.id.user_employee_activity_pick_subcontractor_autoCompleteTextView);

        TLUtil.hideKeyboard(this);

    }


    private void loadSubcontractors() {
// Attach the adapter to a ListView
        final Activity that = this;
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
                            TLUtil.hideKeyboard(that);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    selectedSubcontractor = subcontractors.get(position);

                                    subcontractorTextField.setText(selectedSubcontractor.getName());

                                    pickButton.setEnabled(true);
                                    pickButton.setClickable(true);
                                    pickButton.setAlpha(1.0f);

                                    pickButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(final View v) {
                                            employee.addToSubcontractor(
                                                    selectedSubcontractor,
                                                    new TLJsonHttpResponseHandler(context) {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                            employee.setSubcontractorId(selectedSubcontractor.getId());
                                                            onBackPressed();
                                                        }


                                                        @Override
                                                        public void onFailure(int statusCode, Header[] headers, String
                                                                responseBody, Throwable error) {
                                                            employee.setSubcontractorId(null);
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                            builder
                                                                    .setMessage("Failed to pick a subcontractor, try again.")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                        }
                                                                    });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();

                                                        }
                                                    }

                                            );
                                        }
                                    });


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
    protected void onRestart() {
        super.onRestart();
        loadSubcontractors();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
