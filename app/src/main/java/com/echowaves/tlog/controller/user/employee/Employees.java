package com.echowaves.tlog.controller.user.employee;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.localytics.android.Localytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Employees extends AppCompatActivity {
    private Context context;

    // Construct the data source
    ArrayList<TLEmployee> allEmployees;
    ArrayList<TLEmployee> activeEmployees;
    ArrayList<TLEmployee> inactiveEmployees;

    EmployeesAdapter allEmployeesAdapter;
    EmployeesAdapter activeEmployeesAdapter;
    EmployeesAdapter inactiveEmployeesAdapter;

    ListView listView;

    int selectedSegment = 0;

    private Button backButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("Employees");

        setContentView(R.layout.user_employee_activity_employees);

        context = this;

        backButton = (Button) findViewById(R.id.user_employee_activity_employees_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        addButton = (Button) findViewById(R.id.user_employee_activity_employees_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent employeeCreate = new Intent(TLApplicationContextProvider.getContext(), EmployeeCreate.class);
                startActivity(employeeCreate);

            }
        });

        listView = (ListView) findViewById(R.id.user_employee_activity_employees_listView);

        loadEmployees();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadEmployees();
    }

    private void loadEmployees() {
// Attach the adapter to a ListView

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.user_employee_activity_employees_segmentedView);
        ((RadioButton)radioGroup.getChildAt(selectedSegment)).setChecked(true);

        allEmployees = new ArrayList<TLEmployee>();
        activeEmployees = new ArrayList<TLEmployee>();
        inactiveEmployees = new ArrayList<TLEmployee>();

        // Create the adapter to convert the array to views
        allEmployeesAdapter = new EmployeesAdapter(this, allEmployees);
        activeEmployeesAdapter = new EmployeesAdapter(this, activeEmployees);
        inactiveEmployeesAdapter = new EmployeesAdapter(this, inactiveEmployees);


        TLEmployee.loadAll(
                new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {
                            JSONArray employees = (JSONArray)jsonResponse.get("employees");

                            if(employees.length() == 0 ) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder
                                        .setMessage("No employees yet? Start adding employees before you can see something here.")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }


                            for (int i=0; i<employees.length(); i++) {
                                JSONObject jsonEmployee = employees.getJSONObject(i);
                                TLEmployee employee =
                                        new TLEmployee(
                                                jsonEmployee.getInt("id"),
                                                jsonEmployee.getString("name"),
                                                jsonEmployee.getString("email")
                                        );

                                if(jsonEmployee.getString("activation_code") != null && !jsonEmployee.getString("activation_code").equals("null")) {
                                    employee.setActivationCode(jsonEmployee.getString("activation_code"));
                                    activeEmployees.add(employee);
                                } else {
                                    inactiveEmployees.add(employee);
                                }

                                if(jsonEmployee.getString("subcontractor_id") != null && !jsonEmployee.getString("subcontractor_id").equals("null")) {
                                    employee.setSubcontractorId(jsonEmployee.getInt("subcontractor_id"));
                                }

                                allEmployees.add(employee);

                            }


                            if(selectedSegment == 0) {
                                listView.setAdapter(allEmployeesAdapter);
                            } else if(selectedSegment == 1) {
                                listView.setAdapter(activeEmployeesAdapter);
                            } else if(selectedSegment == 2) {
                                listView.setAdapter(inactiveEmployeesAdapter);
                            }


                            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.user_employee_activity_employees_segmentedView);

                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                            {
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // checkedId is the RadioButton selected

                                    RadioButton rb=(RadioButton)findViewById(checkedId);
                                    if(rb.getText().equals("all")) {
                                        listView.setAdapter(allEmployeesAdapter);
                                        selectedSegment = 0;
                                    } else if(rb.getText().equals("active")) {
                                        listView.setAdapter(activeEmployeesAdapter);
                                        selectedSegment = 1;
                                    } else if(rb.getText().equals("inactive")) {
                                        listView.setAdapter(inactiveEmployeesAdapter);
                                        selectedSegment = 2;
                                    }
                                }
                            });



                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                                    TLEmployee employee = null;
                                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.user_employee_activity_employees_segmentedView);

                                    RadioButton rb=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());

                                    if(rb.getText().equals("all")) {
                                        employee = allEmployees.get(position);
                                    } else if(rb.getText().equals("active")) {
                                        employee = activeEmployees.get(position);
                                    } else if(rb.getText().equals("inactive")) {
                                        employee = inactiveEmployees.get(position);
                                    }

                                    TLApplicationContextProvider.getContext().setCurrentActivityObject(employee);

                                    Intent employeeDetails = new Intent(TLApplicationContextProvider.getContext(), EmployeeDetails.class);
                                    startActivity(employeeDetails);

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
