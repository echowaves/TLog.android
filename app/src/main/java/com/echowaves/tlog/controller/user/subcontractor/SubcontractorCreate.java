package com.echowaves.tlog.controller.user.subcontractor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.localytics.android.Localytics;

import org.apache.commons.validator.GenericValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SubcontractorCreate extends AppCompatActivity {

    private Button backButton;

    private EditText nameTextFeild;
    private Button createButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("SubcontractorCreate");

        setContentView(R.layout.user_subcontractor_activity_subcontractor_create);

        backButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_create_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });


        // show soft keyboard automagically
        nameTextFeild = (EditText) findViewById(R.id.user_subcontractor_activity_subcontractor_create_name_EditText);
        createButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_create_create_Button);

        nameTextFeild.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

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


                    final TLSubcontractor subcontractor = new TLSubcontractor(
                            null,
                            nameTextFeild.getText().toString()
                    );

                    subcontractor.create(
                            new TLJsonHttpResponseHandler(v.getContext()) {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                    Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                                    try {
                                        subcontractor.setId(jsonResponse.getJSONObject("subcontractor").getInt("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    TLApplicationContextProvider.getContext().setCurrentActivityObject(subcontractor);

                                    Intent subcontractorDetails = new Intent(TLApplicationContextProvider.getContext(), SubcontractorDetails.class);
                                    startActivity(subcontractorDetails);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder
                                            .setMessage("Unable to create an subcontractor. Try again.")
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
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
