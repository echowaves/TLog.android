package com.echowaves.tlog.controller.user.subcontractor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;

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

public class Subcontractors extends AppCompatActivity {
    private Context context;

    // Construct the data source
    ArrayList<TLSubcontractor> subcontractors;

    SubcontractorsAdapter subcontractorsAdapter;

    ListView listView;


    private Button backButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("Subcontractors");

        setContentView(R.layout.user_subcontractor_activity_subcontractors);

        context = this;

        backButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractors_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        addButton = (Button) findViewById(R.id.user_subcontractor_activity_subcontractors_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent subcontractorCreate = new Intent(TLApplicationContextProvider.getContext(), SubcontractorCreate.class);
                startActivity(subcontractorCreate);
            }
        });

        listView = (ListView) findViewById(R.id.user_subcontractor_activity_subcontractors_listView);

        loadSubcontractors();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadSubcontractors();
    }

    private void loadSubcontractors() {
// Attach the adapter to a ListView


        subcontractors = new ArrayList<TLSubcontractor>();


        TLSubcontractor.loadAll(
                new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {
                            JSONArray subcontractorsJson = (JSONArray)jsonResponse.get("subcontractors");

                            if(subcontractorsJson.length() == 0 ) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder
                                        .setMessage("No subcontractors yet? Start adding subcontractors before you can see something here.")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }


                            for (int i=0; i<subcontractorsJson.length(); i++) {
                                JSONObject jsonSubcontractor = subcontractorsJson.getJSONObject(i);
                                TLSubcontractor subcontractor =
                                        new TLSubcontractor(
                                                jsonSubcontractor.getInt("id"),
                                                jsonSubcontractor.getString("name")
                                        );


                                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", jsonSubcontractor.getString("coi_expires_at"));
                                if(!jsonSubcontractor.getString("coi_expires_at").equals("null")) {
                                    Date coi_expires_at = new DateTime(jsonSubcontractor.getString("coi_expires_at")).toDate();
                                    subcontractor.setCoiExpiresAt(coi_expires_at);
                                }

                                subcontractors.add(subcontractor);

                            }

                            // Create the adapter to convert the array to views
                            subcontractorsAdapter = new SubcontractorsAdapter(context, subcontractors);

                            listView.setAdapter(subcontractorsAdapter);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                                    TLSubcontractor subcontractor = null;

                                    subcontractor = subcontractors.get(position);

                                    TLApplicationContextProvider.getContext().setCurrentActivityObject(subcontractor);

                                    Intent subcontractorDetails = new Intent(TLApplicationContextProvider.getContext(), SubcontractorDetails.class);
                                    startActivity(subcontractorDetails);

                                }
                            });



                        } catch (JSONException exception) {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }


//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
//                    }
                }
        );

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
