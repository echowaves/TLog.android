package com.echowaves.tlog.controller.employee;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.model.TLCheckin;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.localytics.android.Localytics;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;

public class CheckinDetails extends AppCompatActivity {
    private Context context;

    private Button backButton;
    private Button deleteButton;

    private EditText checkInAtText;
    private EditText durationText;
    private EditText actionCodeText;


    TLCheckin checkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("CheckinDetails");

        context = this;

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

        checkInAtText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                    final View dialogView = View.inflate(context, R.layout.dialog_date_time_picker, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    dialogView.findViewById(R.id.dialog_date_time_picker_setbutton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dialog_date_time_picker_date);
                            TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_date_time_picker_time);
                            Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth(),
                                    timePicker.getCurrentHour(),
                                    timePicker.getCurrentMinute());
                            SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm:ss");
                            String time = mSDF.format(calendar.getTime());
                            int day = datePicker.getDayOfMonth();
                            int month = datePicker.getMonth();
                            int year = datePicker.getYear();
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                            String formatedDate = sdf.format(new Date(year-1900, month, day));
//                            editText_datetime.setText(formatedDate + ' ' + time);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(dialogView);
                    alertDialog.show();


            }
        });




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
