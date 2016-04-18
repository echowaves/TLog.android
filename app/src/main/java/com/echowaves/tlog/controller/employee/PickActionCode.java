package com.echowaves.tlog.controller.employee;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.user.employee.EmployeeActionCodesCompletionsAdapter;
import com.echowaves.tlog.model.TLActionCode;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class PickActionCode extends AppCompatActivity {
    private Context context;
    private TLEmployee employee;

    private ArrayList<TLActionCode> employeesActionCodes;
    private ArrayList<TLActionCode> completions;
    private TLActionCode selectedActionCode;
    private Date checkinTime;

    private PickActionCodeAdapter actionCodeAdapter;

    private ListView listView;

    private Button backButton;
    private Button checkoutButton;

    private AutoCompleteTextView actionCodeTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_pick_action_code);

        context = this;

        backButton = (Button) findViewById(R.id.employee_activity_pick_action_code_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        checkoutButton = (Button) findViewById(R.id.employee_activity_pick_action_code_checkoutButton);

        employee = (TLEmployee) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        actionCodeTextField = (AutoCompleteTextView) findViewById(R.id.employee_activity_pick_action_code_autoCompleteTextView);

        TLActionCode.allActionCodesForEmployee(employee,
                new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {
                            JSONArray jsonActionCodes = (JSONArray) jsonResponse.get("actionCodes");
                            employeesActionCodes = new ArrayList<TLActionCode>();

                            for (int i = 0; i < jsonActionCodes.length(); i++) {
                                JSONObject jsonActionCode = jsonActionCodes.getJSONObject(i);
                                TLActionCode actionCode =
                                        new TLActionCode(
                                                jsonActionCode.getInt("id"),
                                                jsonActionCode.getString("code"),
                                                jsonActionCode.getString("description")
                                        );
                                // Create the adapter to convert the array to views
                                employeesActionCodes.add(actionCode);

                            }

                            if(employeesActionCodes.size() > 0) {
                                actionCodeTextField.setEnabled(false);
                                actionCodeTextField.setHint("pick from the list");
                            }


                            listView = (ListView) findViewById(R.id.employee_activity_pick_action_code_listView);
                            actionCodeAdapter = new PickActionCodeAdapter(context, employeesActionCodes);
                            listView.setAdapter(actionCodeAdapter);


//                            actionCodeTextField.setThreshold(1);//will start working from first character
//                            actionCodeTextField.setAdapter(completionsAdapter);//setting the adapter data into the AutoCompleteTextView
//
//
//                            actionCodeTextField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                    TLActionCode actionCode = null;
//                                    actionCode = completionsActionCodes.get(position);
//
////                                    add action code here
//                                    employee.addActionCode(actionCode, new TLJsonHttpResponseHandler(context) {
//                                        @Override
//                                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
//                                            actionCodeTextField.clearListSelection();
//                                            actionCodeTextField.setText("");
//                                            loadActionCodes();
//                                        }
//                                    });
//                                }
//
//                            });

                        } catch (
                                JSONException exception
                                )

                        {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }


                }

        );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
