package com.echowaves.tlog.controller.user.employee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.apache.commons.validator.GenericValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

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

        nameTextFeild.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        subContractorSwitch = (Switch) findViewById(R.id.user_employee_activity_employee_details_sub_Switch);
        subContractorSwitch.setChecked(employee.getSubcontractor());
        subContractorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                employee.setSubcontractor(isChecked);
                saveButtonClicked(buttonView);
            }
        });

        isActiveSwitch = (Switch) findViewById(R.id.user_employee_activity_employee_details_active_Switch);
        isActiveSwitch.setChecked(employee.isActive());

        isActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton v, boolean isChecked) {
                if (isChecked) {
                    employee.activate(
                            new TLJsonHttpResponseHandler(v.getContext()) {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                    Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());


                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder
                                            .setMessage("Employee successfuly activated, activation email is sent.")
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
                                            .setMessage("Error activating employee, try again.")
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
                } else {
                    employee.deactivate(
                            new TLJsonHttpResponseHandler(v.getContext()) {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                    Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder
                                            .setMessage("Employee successfuly deactivated.")
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
                                            .setMessage("Error deactivating employee, try again.")
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


            }
        });


        saveButton = (Button) findViewById(R.id.user_employee_activity_employee_details_save_Button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                saveButtonClicked(v);
            }
        });


        actionCodesButton = (Button) findViewById(R.id.user_employee_activity_employee_details_actionCodes_Button);
        actionCodesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                TLApplicationContextProvider.getContext().setCurrentActivityObject(employee);

                Intent employeeActionCodes = new Intent(TLApplicationContextProvider.getContext(), EmployeeActionCodes.class);
                startActivity(employeeActionCodes);
            }
        });


    }


    void saveButtonClicked(final View v) {
        // validating code
        ArrayList<String> validationErrors = new ArrayList<>();
        if (GenericValidator.isBlankOrNull(emailTextField.getText().toString())) {
            validationErrors.add("Email is required.");
        }
        if (GenericValidator.isBlankOrNull(nameTextFeild.getText().toString())) {
            validationErrors.add("Name is required.");
        }

        if (!GenericValidator.isEmail(emailTextField.getText().toString())) {
            validationErrors.add("Wrong email format.");
        }

        if (!GenericValidator.maxLength(nameTextFeild.getText().toString(), 100)) {
            validationErrors.add("Name can't be longer than 100.");
        }
        if (!GenericValidator.maxLength(emailTextField.getText().toString(), 100)) {
            validationErrors.add("Email can't be longer than 100.");
        }
        // validating code


        if (validationErrors.size() == 0) { // no validation errors, proceed

            employee.setName(nameTextFeild.getText().toString());
            employee.setEmail(emailTextField.getText().toString());
            employee.setSubcontractor(subContractorSwitch.isChecked());

            employee.update(
                    new TLJsonHttpResponseHandler(v.getContext()) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                            Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder
                                    .setMessage("Employee successfuly updated.")
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
                                    .setMessage("Error, perhaps an employee with the same email already exists.")
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

        Intent employees = new Intent(TLApplicationContextProvider.getContext(), Employees.class);
        employees.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        employees.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(employees);

        return;
    }

}
