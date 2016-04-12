package com.echowaves.tlog.controller.user.employee;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLActionCode;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class EmployeeActionCodes extends AppCompatActivity {
    private Context context;

    private TLEmployee employee;


    private Button backButton;
    private TextView title;
    private AutoCompleteTextView actionCodeTextField;

    private ListView listView;

    ArrayList<TLActionCode> actionCodes;

    ActionCodesAdapter actionCodesAdapter;

    ArrayList<TLActionCode> completionsActionCodes;
    ActionCodesCompletionsAdapter completionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_employee_activity_employee_action_codes);
        context = this;

        employee = (TLEmployee) TLApplicationContextProvider.getContext().getCurrentActivityObject();


        backButton = (Button) findViewById(R.id.user_employee_activity_employee_action_codes_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        title = (TextView) findViewById(R.id.user_employee_activity_employee_action_codes_title);
        title.setText(employee.getName());


        listView = (ListView) findViewById(R.id.user_employee_activity_employee_action_codes_actionCode_listView);

        loadActionCodes();


        actionCodeTextField = (AutoCompleteTextView) findViewById(R.id.user_employee_activity_employee_action_codes_actionCode_AutoCompleteTextView);
        actionCodeTextField.setThreshold(1);//will start working from first character


        actionCodeTextField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                TLActionCode.autoComplete(s.toString(),
                        new TLJsonHttpResponseHandler(context) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                try {
                                    JSONArray jsonActionCodes = (JSONArray) jsonResponse.get("actionCodes");
                                    completionsActionCodes = new ArrayList<TLActionCode>();

                                    for (int i = 0; i < jsonActionCodes.length(); i++) {
                                        JSONObject jsonActionCode = jsonActionCodes.getJSONObject(i);
                                        TLActionCode actionCode =
                                                new TLActionCode(
                                                        jsonActionCode.getInt("id"),
                                                        jsonActionCode.getString("code"),
                                                        jsonActionCode.getString("description")
                                                );
                                        // Create the adapter to convert the array to views
                                        completionsActionCodes.add(actionCode);

                                    }

                                    completionsAdapter = new ActionCodesCompletionsAdapter(context, completionsActionCodes);


                                    actionCodeTextField.setThreshold(1);//will start working from first character
                                    actionCodeTextField.setAdapter(completionsAdapter);//setting the adapter data into the AutoCompleteTextView


                                    actionCodeTextField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            TLActionCode actionCode = null;
                                            actionCode = completionsActionCodes.get(position);

//                                    add action code here
                                            employee.addActionCode(actionCode, new TLJsonHttpResponseHandler(context) {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                    actionCodeTextField.clearListSelection();
                                                    actionCodeTextField.setText("");
                                                    loadActionCodes();
                                                }
                                            });
                                        }

                                    });

                                } catch (
                                        JSONException exception
                                        )

                                {
                                    Log.e(getClass().getName(), exception.toString());
                                }
                            }


                            @Override
                            public void onFailure(int statusCode, Header[] headers, String
                                    responseBody, Throwable error) {
                            }
                        }

                );







            }
        });





    }


    private void loadActionCodes() {
// Attach the adapter to a ListView


        actionCodes = new ArrayList<TLActionCode>();

        // Create the adapter to convert the array to views

        actionCodesAdapter = new ActionCodesAdapter(this, actionCodes);

        TLActionCode.allActionCodesForEmployee(
                employee,
                new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {
                            JSONArray jsonActionCodes = (JSONArray) jsonResponse.get("actionCodes");

                            for (int i = 0; i < jsonActionCodes.length(); i++) {
                                JSONObject jsonActionCode = jsonActionCodes.getJSONObject(i);
                                TLActionCode actionCode =
                                        new TLActionCode(
                                                jsonActionCode.getInt("id"),
                                                jsonActionCode.getString("code"),
                                                jsonActionCode.getString("description")
                                        );
                                actionCodes.add(actionCode);
                            }

                            listView.setAdapter(actionCodesAdapter);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    TLActionCode actionCode = null;
                                    actionCode = actionCodes.get(position);

//                                    delete action code here
                                    employee.deleteActionCode(actionCode, new TLJsonHttpResponseHandler(context) {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                            loadActionCodes();
                                        }
                                    });
                                }

                            });

                        } catch (
                                JSONException exception
                                )

                        {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseBody, Throwable error) {
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
