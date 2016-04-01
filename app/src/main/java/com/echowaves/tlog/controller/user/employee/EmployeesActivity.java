package com.echowaves.tlog.controller.user.employee;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class EmployeesActivity extends AppCompatActivity {
    private Context context;

    // Construct the data source
    ArrayList<TLEmployee> allEmployees = new ArrayList<TLEmployee>();
    ArrayList<TLEmployee> activeEmployees = new ArrayList<TLEmployee>();
    ArrayList<TLEmployee> inactiveEmployees = new ArrayList<TLEmployee>();

    EmployeesAdapter allEmployeesAdapter;
    EmployeesAdapter activeEmployeesAdapter;
    EmployeesAdapter inactiveEmployeesAdapter;

    ListView listView;

    private Button backButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                onBackPressed();
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


        allEmployees = new ArrayList<TLEmployee>();
        activeEmployees = new ArrayList<TLEmployee>();
        inactiveEmployees = new ArrayList<TLEmployee>();

        // Create the adapter to convert the array to views
        allEmployeesAdapter = new EmployeesAdapter(this, allEmployees);
        activeEmployeesAdapter = new EmployeesAdapter(this, activeEmployees);
        inactiveEmployeesAdapter = new EmployeesAdapter(this, inactiveEmployees);


        listView.setAdapter(allEmployeesAdapter);


        TLEmployee.loadAll(
                new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {
                            JSONArray employees = (JSONArray)jsonResponse.get("employees");

                            for (int i=0; i<employees.length(); i++) {
                                JSONObject jsonEmployee = employees.getJSONObject(i);
                                TLEmployee employee =
                                        new TLEmployee(
                                                jsonEmployee.getInt("id"),
                                                jsonEmployee.getString("name"),
                                                jsonEmployee.getString("email"),
                                                jsonEmployee.getBoolean("is_subcontractor"),
                                                jsonEmployee.getString("activation_code")
                                        );
                                allEmployees.add(employee);
                                if(employee.getActivationCode() == null) {
                                    inactiveEmployees.add(employee);
                                } else {
                                    activeEmployees.add(employee);
                                }
                            }

                            allEmployeesAdapter.addAll(allEmployees);
                            activeEmployeesAdapter.addAll(activeEmployees);
                            inactiveEmployeesAdapter.addAll(inactiveEmployees);

                        } catch (JSONException exception) {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                    }
                }
        );

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
