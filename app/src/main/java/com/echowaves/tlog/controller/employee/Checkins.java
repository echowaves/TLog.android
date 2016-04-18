package com.echowaves.tlog.controller.employee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.controller.user.SignIn;
import com.echowaves.tlog.model.TLActionCode;
import com.echowaves.tlog.model.TLCheckin;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class Checkins extends AppCompatActivity {
    private Context context;

    private Button signoutButton;
    private TextView title;

    private Button checkinButton;

    private TextView sinceLabel;
    private TextView actionCodeLabel;

    // Construct the data source
    private TLEmployee currentEmployee;
    private ArrayList<TLCheckin> currentCheckins;
    private CheckinsAdapter currentCheckinsAdapter;
    private ListView listView;

    private TLCheckin currentCheckin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_checkins);
        context = this;
        JodaTimeAndroid.init(this);

        signoutButton = (Button) findViewById(R.id.employee_activity_checkins_signOutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        sinceLabel = (TextView) findViewById(R.id.employee_activity_checkins_sinceLabel);
        actionCodeLabel = (TextView) findViewById(R.id.employee_activity_checkins_actionCodeLabel);

        checkinButton = (Button) findViewById(R.id.employee_activity_checkins_checkInButton);
        checkinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                if (currentCheckin == null) {

                    TLApplicationContextProvider.getContext().setCurrentActivityObject(currentEmployee);
                    Intent pickActionCode = new Intent(TLApplicationContextProvider.getContext(), PickActionCode.class);
                    startActivity(pickActionCode);
                } else {
                    //checkout here

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder
                            .setMessage("Sure to checkout?")
                            .setCancelable(true)
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Interval interv = new Interval(new DateTime(currentCheckin.getCheckedInAt()), DateTime.now());

                                    int elapsedTime = (int) (interv.toDurationMillis() / 1000);
                                    if (elapsedTime > 8 * 60 * 60) { // greater then 8 hours
                                        elapsedTime = 8 * 60 * 60;
                                    }

                                    currentCheckin.setDuration(elapsedTime);

                                    currentCheckin.update(
                                            new TLJsonHttpResponseHandler(v.getContext()) {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                    Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                                                    loadCheckins();
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                                    loadCheckins();

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                    builder
                                                            .setMessage("Error checking out. Try again.")
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
            }
        });

        loadCheckins();
    }


    void loadCheckins() {

        TLCheckin.getAllCheckins(0, 100, new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {

                            JSONObject jsonEmployee = jsonResponse.getJSONObject("employee");
                            TLEmployee employee =
                                    new TLEmployee(
                                            jsonEmployee.getInt("id"),
                                            jsonEmployee.getString("name"),
                                            jsonEmployee.getString("email"),
                                            jsonEmployee.getBoolean("is_subcontractor")
                                    );


                            JSONArray jsonCheckins = (JSONArray) jsonResponse.get("checkins");

                            ArrayList<TLCheckin> checkins = new ArrayList<TLCheckin>();

                            for (int i = 0; i < jsonCheckins.length(); i++) {
                                JSONObject jsonCheckin = jsonCheckins.getJSONObject(i);


                                TLActionCode actionCode =
                                        new TLActionCode(
                                                jsonCheckin.getInt("action_code_id"),
                                                jsonCheckin.getString("code"),
                                                jsonCheckin.getString("description"));


//                                2015-12-22 22:34:51.326
                                Date checkinDate = new DateTime(jsonCheckin.getString("checked_in_at")).toDate();


                                Integer interval =
                                        jsonCheckin.getJSONObject("duration").optInt("hours", 0) * 60 * 60
                                                + jsonCheckin.getJSONObject("duration").optInt("minutes", 0) * 60
                                                + jsonCheckin.getJSONObject("duration").optInt("seconds", 0);


                                TLCheckin checkin =
                                        new TLCheckin(
                                                jsonCheckin.getInt("id"),
                                                jsonCheckin.getString("email"),
                                                jsonCheckin.getInt("user_id"),
                                                checkinDate,
                                                interval,
                                                actionCode);


                                checkins.add(checkin);
                            }

                            currentEmployee = employee;
                            currentCheckins = checkins;
                            currentCheckin = null;

                            //iterate over checkins and find a current checkin that is active (duration == 0)
                            for (int index = 0; index < checkins.size(); index++) {
                                if (checkins.get(index).getDuration() == 0) {
                                    if (currentCheckin == null) {
                                        currentCheckin = checkins.get(index);
                                        currentCheckins.remove(index);
                                    }
                                }
                            }
                            updateViews();

                        } catch (JSONException exception) {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder
                                .setMessage("Error loading checkins. Try again.")
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

    void updateViews() {
        title = (TextView) findViewById(R.id.employee_activity_checkins_title);
        title.setText(currentEmployee.getName());


        if (this.currentCheckin != null) {
            this.checkinButton.setText("Check Out");
            this.checkinButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_red_button));  //red
            this.sinceLabel.setVisibility(View.VISIBLE);
            this.actionCodeLabel.setVisibility(View.VISIBLE);
            this.sinceLabel.setText("Since:\n" + new DateTime(this.currentCheckin.getCheckedInAt()).toString(TLConstants.defaultDateFormat));
            this.actionCodeLabel.setText(this.currentCheckin.getActionCode().getCode() + ":" + this.currentCheckin.getActionCode().getDescr());
        } else {
            this.checkinButton.setText("Check In");
            this.checkinButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_green_button));  //green
            this.sinceLabel.setVisibility(View.INVISIBLE);
            this.actionCodeLabel.setVisibility(View.INVISIBLE);
        }


        listView = (ListView) findViewById(R.id.employee_activity_checkins_listView);
        currentCheckinsAdapter = new CheckinsAdapter(this, currentCheckins);

        listView.setAdapter(currentCheckinsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TLCheckin checkin = currentCheckins.get(position);

                Interval interv = new Interval(new DateTime(checkin.getCheckedInAt()), DateTime.now());

                int elapsedTime = (int) (interv.toDurationMillis() / 1000);

                if (elapsedTime > 7 * 24 * 60 * 60) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder
                            .setMessage("Can not update checkins older then 7 days.")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {

                    TLApplicationContextProvider.getContext().setCurrentActivityObject(checkin);

                    Intent checkinDetails = new Intent(TLApplicationContextProvider.getContext(), CheckinDetails.class);
                    startActivity(checkinDetails);
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        TLEmployee.clearActivationCodeFromLocalStorage();

        Intent signIn = new Intent(TLApplicationContextProvider.getContext(), SignIn.class);
        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        signIn.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(signIn);

        return;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadCheckins();
    }

}
