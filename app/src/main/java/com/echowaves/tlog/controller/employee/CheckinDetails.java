package com.echowaves.tlog.controller.employee;

import android.app.AlertDialog;
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

                final Date originalDate = checkin.getCheckedInAt();

                final View dateTimePickerDialog = View.inflate(context, R.layout.dialog_date_time_picker, null);
                final DatePicker datePicker = (DatePicker) dateTimePickerDialog.findViewById(R.id.dialog_date_time_picker_date);
                final TimePicker timePicker = (TimePicker) dateTimePickerDialog.findViewById(R.id.dialog_date_time_picker_time);

                final Calendar cal = Calendar.getInstance();
                cal.setTime(checkin.getCheckedInAt());
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                dateTimePickerDialog.findViewById(R.id.dialog_date_time_picker_setbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        checkin.setCheckedInAt(calendar.getTime());
                        checkin.update(
                                new TLJsonHttpResponseHandler(view.getContext()) {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                        Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                                        checkInAtText.setText(new DateTime(checkin.getCheckedInAt()).toString(TLConstants.defaultDateFormat));
                                        alertDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        //reset the pickerss
                                        cal.setTime(originalDate);
                                        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                                        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                                        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));


                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder
                                                .setMessage("Unable to update date, can only update between now and 7 days ago, try again.")
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
                alertDialog.setView(dateTimePickerDialog);
                alertDialog.show();
            }
        });


        durationText = (EditText) findViewById(R.id.employee_activity_checkin_details_durationText);
        durationText.setText(checkin.getDurationExtendedText());
        durationText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final View durationPickerDialog = View.inflate(context, R.layout.dialog_duration_picker, null);
                final TimePicker timePicker = (TimePicker) durationPickerDialog.findViewById(R.id.dialog_duration_picker_time);

                timePicker.setIs24HourView(true);


                timePicker.setCurrentHour(checkin.getDuration()/3600);
                timePicker.setCurrentMinute((checkin.getDuration()% 3600) / 60);

                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                durationPickerDialog.findViewById(R.id.dialog_duration_picker_setbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        checkin.setDuration(timePicker.getCurrentHour()*3600 + timePicker.getCurrentMinute()*60);
                        checkin.update(
                                new TLJsonHttpResponseHandler(view.getContext()) {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                        Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                                        durationText.setText(checkin.getDurationExtendedText());
                                        alertDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        //reset the pickerss

                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder
                                                .setMessage("Unable to update duration, try again.")
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
                alertDialog.setView(durationPickerDialog);
                alertDialog.show();
            }
        });



        actionCodeText = (EditText) findViewById(R.id.employee_activity_checkin_details_actionCodeText);
        actionCodeText.setText(checkin.getActionCode().getCode() + ":" + checkin.getActionCode().getDescr());


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
