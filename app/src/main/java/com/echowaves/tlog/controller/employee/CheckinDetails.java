package com.echowaves.tlog.controller.employee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.model.TLCheckin;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CheckinDetails extends AppCompatActivity {

    private Button backButton;
    private Button deleteButton;

    private EditText checkInAtText;
    private EditText durationText;
    private EditText actionCodeText;


    TLCheckin checkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_checkin_details);

        checkin = (TLCheckin) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        backButton = (Button) findViewById(R.id.employee_activity_checkin_details_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        deleteButton = (Button) findViewById(R.id.employee_activity_checkin_details_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder
                        .setMessage("Are you sure want to delete the checkin?")
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                checkin.delete(
                                        new TLJsonHttpResponseHandler(v.getContext()) {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                builder
                                                        .setMessage("Checkin successfuly deleted.")
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
                                                        .setMessage("Error deleting checkin, try again.")
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

        checkInAtText = (EditText) findViewById(R.id.employee_activity_checkin_details_checkInAtText);
        checkInAtText.setText(new DateTime(checkin.getCheckedInAt()).toString(TLConstants.defaultDateFormat));

        durationText = (EditText) findViewById(R.id.employee_activity_checkin_details_durationText);
        durationText.setText(checkin.getDurationExtendedText());

        actionCodeText = (EditText) findViewById(R.id.employee_activity_checkin_details_actionCodeText);
        actionCodeText.setText(checkin.getActionCode().getCode() + ":" + checkin.getActionCode().getDescr());


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
