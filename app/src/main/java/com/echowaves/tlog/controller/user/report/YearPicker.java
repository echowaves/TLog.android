package com.echowaves.tlog.controller.user.report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLReport;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.localytics.android.Localytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class YearPicker extends AppCompatActivity {
    private Context context;

    private Button backButton;

    private ListView listView;

    ArrayList<String> years;
    YearPickerAdapter yearPickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("YearPicker");

        setContentView(R.layout.user_report_activity_year_picker);
        context = this;

        backButton = (Button) findViewById(R.id.user_reports_activity_year_picker_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });


        listView = (ListView) findViewById(R.id.user_reports_activity_year_picker_listView);



        TLReport.yearsForUser(new TLJsonHttpResponseHandler(context) {
                                  @Override
                                  public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                      try {

                                          JSONArray jsonYears = jsonResponse.getJSONArray("years");


                                          if(jsonYears.length() == 0 ) {
                                              AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                              builder
                                                      .setMessage("No any reports yet? Have your employees start checking in and checking out every day -- then you will be able to see the beautiful reports here.")
                                                      .setCancelable(false)
                                                      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                          public void onClick(DialogInterface dialog, int id) {
                                                          }
                                                      });
                                              AlertDialog alert = builder.create();
                                              alert.show();
                                          }



                                          years = new ArrayList<String>();


                                          for (int i = 0; i < jsonYears.length(); i++) {
                                              String year = jsonYears.getJSONObject(i).getString("date_part");

                                              years.add(year);
                                          }

                                          yearPickerAdapter = new YearPickerAdapter(context, years);
                                          listView.setAdapter(yearPickerAdapter);

                                      } catch (JSONException exception) {
                                          Log.e(getClass().getName(), exception.toString());
                                      }
                                  }
                              }
        );

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
