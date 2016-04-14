package com.echowaves.tlog.controller.employee;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.user.SignIn;
import com.echowaves.tlog.controller.user.employee.EmployeeDetails;
import com.echowaves.tlog.model.TLActionCode;
import com.echowaves.tlog.model.TLCheckin;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class    EmployeeHome extends AppCompatActivity {
    private Context context;

    private Button signoutButton;

    private TLEmployee currentEmployee;
    private ArrayList<TLCheckin> currentCheckins;
    private TLCheckin currentCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_employee_home);
        context = this;

        signoutButton = (Button) findViewById(R.id.employee_activity_employee_home_signOutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });


        loadCheckins();
    }


    void loadCheckins() {

        TLCheckin.getAllCheckins(0, 100 , new TLJsonHttpResponseHandler(context) {
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




                            JSONArray jsonCheckins = (JSONArray)jsonResponse.get("checkins");

                            ArrayList<TLCheckin> checkins = new ArrayList<TLCheckin>();

                            for (int i=0; i<jsonCheckins.length(); i++) {
                                JSONObject jsonCheckin = jsonCheckins.getJSONObject(i);


                                TLActionCode actionCode =
                                        new TLActionCode(
                                                jsonCheckin.getInt("action_code_id"),
                                        jsonCheckin.getString("code"),
                                        jsonCheckin.getString("description"));


//                                2015-12-22 22:34:51.326
                                Date checkinDate = new DateTime(jsonCheckin.getString("checked_in_at")).toDate();

                                        Integer interval =
                                                jsonCheckin.getJSONObject("duration").getInt("hours") * 60 * 60 +
                                                jsonCheckin.getJSONObject("duration").getInt("minutes") * 60 +
                                                        jsonCheckin.getJSONObject("duration").getInt("seconds");



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
                            for(int index = 0; index < checkins.size(); index++) {
                                if(checkins.get(index).getDuration() == 0) {
                                    if( currentCheckin == null) {
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
//        navBar.topItem?.title = employee.name
//        if (self.currentCheckin != nil) {
//            self.checkinButton!.setTitle("Check Out", forState: .Normal)
//            self.checkinButton.backgroundColor = UIColor(rgb: 0xFF0000) //red
//            self.sinceLabel.hidden = false
//            self.actionCodeLabel.hidden = false
//            self.sinceLabel.text = "Since:\n\(defaultDateFormatter.stringFromDate((self.currentCheckin.checkedInAt)!))"
//            self.actionCodeLabel.text = "\((self.currentCheckin.actionCode?.code)!):\((self.currentCheckin.actionCode?.descr)!)"
//        } else {
//            self.checkinButton!.setTitle("Check In", forState: .Normal)
//            self.checkinButton.backgroundColor = UIColor(rgb: 0x00C333) //green
//            self.sinceLabel.hidden = true
//            self.actionCodeLabel.hidden = true
//        }
//
//        dispatch_async(dispatch_get_main_queue(),{ ()->() in
//                self.tableView.reloadData()
//        })
//        //        self.tableView.reloadInputViews()
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

}
