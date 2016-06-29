package com.echowaves.tlog.controller.user.subcontractor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.employee.Checkins;
import com.echowaves.tlog.controller.user.employee.EmployeeActionCodes;
import com.echowaves.tlog.controller.user.employee.Employees;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.model.TLUser;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.echowaves.tlog.util.TLUtil;
import com.localytics.android.Localytics;

import org.apache.commons.validator.GenericValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SubcontractorDetails extends AppCompatActivity {
    private Context context;
    private Activity activity;

    private TLSubcontractor subcontractor;


    private Button backButton;
    private Button deleteButton;

    private EditText nameTextFeild;
    private Button saveButton;
    private EditText coiExpiresAtTextFeild;

    private ImageView imageView;

    private Button downloadButton;
    private Button photoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("SubcontractorDetails");

        setContentView(R.layout.user_subcontractor_activity_subcontractor_details);
        this.context = this;
        this.activity = this;

        subcontractor = (TLSubcontractor) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        Log.d(getClass().getName(), "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + subcontractor.getName());

        backButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_details_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        saveButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_details_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                saveButtonClicked(v);
            }
        });


        deleteButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_details_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder
                        .setMessage("Are you sure want to delete the subcontractor?")
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                subcontractor.delete(
                                        new TLJsonHttpResponseHandler(v.getContext()) {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                builder
                                                        .setMessage("Subcontractor successfuly deleted.")
                                                        .setCancelable(false)
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                onBackPressed();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                builder
                                                        .setMessage("Error deleting subcontractor, try again.")
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
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        // show soft keyboard automagically
        nameTextFeild = (EditText) findViewById(R.id.user_subcontractor_activity_subcontractor_details_nameEditText);
        nameTextFeild.setText(subcontractor.getName());

        TLUtil.hideKeyboard(activity);



    }


    void saveButtonClicked(final View v) {
        TLUtil.hideKeyboard(activity);
        // validating code
        ArrayList<String> validationErrors = new ArrayList<>();
        if (GenericValidator.isBlankOrNull(nameTextFeild.getText().toString())) {
            validationErrors.add("Name is required.");
        }

        if (!GenericValidator.maxLength(nameTextFeild.getText().toString(), 100)) {
            validationErrors.add("Name can't be longer than 100.");
        }
        // validating code


        if (validationErrors.size() == 0) { // no validation errors, proceed

            subcontractor.setName(nameTextFeild.getText().toString());

            subcontractor.update(
                    new TLJsonHttpResponseHandler(v.getContext()) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                            Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder
                                    .setMessage("Subcontractor successfuly updated.")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder
                                    .setMessage("Error, try again.")
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

        } else { // validation failed
            String errorString = "";
            for (String error : validationErrors) {
                errorString += error + "\n\n";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder
                    .setMessage(errorString)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent subcontractors = new Intent(TLApplicationContextProvider.getContext(), Subcontractors.class);
        subcontractors.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        subcontractors.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(subcontractors);
        return;
    }

}
