package com.echowaves.tlog.controller.user.subcontractor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.controller.employee.Checkins;
import com.echowaves.tlog.controller.user.employee.EmployeeActionCodes;
import com.echowaves.tlog.controller.user.employee.Employees;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.model.TLUser;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.echowaves.tlog.util.TLUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.localytics.android.Localytics;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    SimpleArcDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("SubcontractorDetails");

        setContentView(R.layout.user_subcontractor_activity_subcontractor_details);
        this.context = this;
        this.activity = this;

        mDialog = new SimpleArcDialog(this);

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
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(final View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder
//                        .setMessage("Are you sure want to delete the subcontractor?")
//                        .setCancelable(true)
//                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                            }
//                        })
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                subcontractor.delete(
//                                        new TLJsonHttpResponseHandler(v.getContext()) {
//                                            @Override
//                                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
//                                                Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
//
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                                                builder
//                                                        .setMessage("Subcontractor successfuly deleted.")
//                                                        .setCancelable(false)
//                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                                onBackPressed();
//                                                            }
//                                                        });
//                                                AlertDialog alert = builder.create();
//                                                alert.show();
//                                            }
//
//                                            @Override
//                                            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                                                builder
//                                                        .setMessage("Error deleting subcontractor, try again.")
//                                                        .setCancelable(false)
//                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int id) {
//                                                            }
//                                                        });
//                                                AlertDialog alert = builder.create();
//                                                alert.show();
//                                            }
//                                        }
//                                );
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });


        // show soft keyboard automagically
        nameTextFeild = (EditText) findViewById(R.id.user_subcontractor_activity_subcontractor_details_nameEditText);
        nameTextFeild.setText(subcontractor.getName());

        TLUtil.hideKeyboard(activity);

        coiExpiresAtTextFeild = (EditText) findViewById(R.id.user_subcontractor_activity_subcontractor_details_coiExpiresAtEditText);
        if (subcontractor.getCoiExpiresAt() != null) {
            coiExpiresAtTextFeild.setText(new DateTime(subcontractor.getCoiExpiresAt()).toString(TLConstants.shortDateFormat));
        }

        coiExpiresAtTextFeild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subcontractor.getCoiExpiresAt() == null) {
                    subcontractor.setCoiExpiresAt(new DateTime().plusYears(1).toDate());
                }
                final Date originalDate = subcontractor.getCoiExpiresAt();


                final View datePickerDialog = View.inflate(context, R.layout.dialog_date_picker, null);
                final DatePicker datePicker = (DatePicker) datePickerDialog.findViewById(R.id.dialog_date_picker_date);

                datePicker.setMaxDate(new DateTime().plusYears(10).toDate().getTime());
                datePicker.setMinDate(new Date().getTime());

                final Calendar cal = Calendar.getInstance();
                cal.setTime(subcontractor.getCoiExpiresAt());
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                datePickerDialog.findViewById(R.id.dialog_date_picker_setbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());

                        subcontractor.setCoiExpiresAt(calendar.getTime());
                        subcontractor.update(
                                new TLJsonHttpResponseHandler(view.getContext()) {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                        Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                                        coiExpiresAtTextFeild.setText(new DateTime(subcontractor.getCoiExpiresAt()).toString(TLConstants.shortDateFormat));
                                        alertDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        //reset the pickerss
                                        cal.setTime(originalDate);
                                        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder
                                                .setMessage("Unable to update date, try again.")
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
                alertDialog.setView(datePickerDialog);
                alertDialog.show();
            }
        });


        imageView = (ImageView) findViewById(R.id.user_subcontractor_activity_subcontractor_details_imageView);


        subcontractor.downloadCOI(new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                ArcConfiguration configuration = new ArcConfiguration(context);
                configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);

                configuration.setText("Loading...");


                mDialog.setConfiguration(configuration);
                mDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] imageAsBytes) {
                try {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    imageView.refreshDrawableState();

                    mDialog.dismiss();
                } catch (Throwable e) {
                    e.printStackTrace();
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                downloadButton.setVisibility(View.GONE);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                //just a place holder to suppress excessive output
            }

        });


        downloadButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_details_downloadButton);
        photoButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractor_details_photoButton);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
