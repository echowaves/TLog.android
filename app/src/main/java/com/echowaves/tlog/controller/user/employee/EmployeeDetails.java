package com.echowaves.tlog.controller.user.employee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EmployeeDetails extends AppCompatActivity {

    private TLEmployee employee;


    private Button backButton;
    private Button deleteButton;

    private EditText nameTextFeild;
    private EditText emailTextField;

    private Switch subContractorSwitch;
    private Switch isActiveSwitch;

    private Button saveButton;
    private Button actionCodesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_employee_activity_employee_details);

        employee = (TLEmployee) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        Log.d(getClass().getName(), "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + employee.getName());

        backButton = (Button) findViewById(R.id.user_employee_activity_employee_details_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        deleteButton = (Button) findViewById(R.id.user_employee_activity_employee_details_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder
                        .setMessage("Are you sure want to delete the employee?")
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                employee.delete(
                                        new TLJsonHttpResponseHandler(v.getContext()) {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                builder
                                                        .setMessage("Employee successfuly deleted.")
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
                                                        .setMessage("Error deleting employee, try again.")
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


        // show soft keyboard automagically
        nameTextFeild = (EditText) findViewById(R.id.user_employee_activity_employee_details_name_EditText);
        nameTextFeild.setText(employee.getName());
        emailTextField = (EditText) findViewById(R.id.user_employee_activity_employee_details_email_EditText);
        emailTextField.setText(employee.getEmail());
        saveButton = (Button) findViewById(R.id.user_employee_activity_employee_details_save_Button);

        nameTextFeild.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        subContractorSwitch = (Switch) findViewById(R.id.user_employee_activity_employee_details_sub_Switch);
        subContractorSwitch.setChecked(employee.getSubcontractor());
        isActiveSwitch = (Switch) findViewById(R.id.user_employee_activity_employee_details_active_Switch);
        isActiveSwitch.setChecked(employee.isActive());
//
        saveButton = (Button) findViewById(R.id.user_employee_activity_employee_details_save_Button);
        actionCodesButton = (Button) findViewById(R.id.user_employee_activity_employee_details_actionCodes_Button);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent employees = new Intent(TLApplicationContextProvider.getContext(), EmployeesActivity.class);
        employees.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        employees.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(employees);

        return;
    }

}
