package com.echowaves.tlog.controller.user.employee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.employee.Checkins;
import com.echowaves.tlog.controller.user.subcontractor.SubcontractorDetails;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.model.TLSubcontractor;
import com.echowaves.tlog.model.TLUser;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.echowaves.tlog.util.TLUtil;
import com.localytics.android.Localytics;

import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class EmployeeDetails extends AppCompatActivity {
    private Context context;
    private Activity activity;

    private TLEmployee employee;
    private TLSubcontractor subcontractor;

    private Button backButton;
    private Button deleteButton;

    private EditText nameTextFeild;
    private EditText emailTextField;

    private Switch isSubContractorSwitch;
    private Switch isActiveSwitch;
    private Button subcontractorNameButton;

    private Button saveButton;
    private Button actionCodesButton;
    private Button checkinsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("EmployeeDetails");

        setContentView(R.layout.user_employee_activity_employee_details);
        this.context = this;
        this.activity = this;

        employee = (TLEmployee) TLApplicationContextProvider.getContext().getCurrentActivityObject();

//        Log.d(getClass().getName(), "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + employee.getName());

        backButton = (Button) findViewById(R.id.user_employee_activity_employee_details_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
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

        checkinsButton = (Button) findViewById(R.id.user_employee_activity_employee_details_checkins_Button);
        checkinsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                TLUser.setUserLogin();
                TLEmployee.storeActivationCodeLocally(employee.getActivationCode());

                Intent checkins = new Intent(TLApplicationContextProvider.getContext(), Checkins.class);
                startActivity(checkins);
            }
        });


        deleteButton = (Button) findViewById(R.id.user_employee_activity_employee_details_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                deleteClicked(v);

            }
        });


        // show soft keyboard automagically
        nameTextFeild = (EditText) findViewById(R.id.user_employee_activity_employee_details_name_EditText);
        nameTextFeild.setText(employee.getName());
        emailTextField = (EditText) findViewById(R.id.user_employee_activity_employee_details_email_EditText);
        emailTextField.setText(employee.getEmail());

        TLUtil.hideKeyboard(activity);
//        nameTextFeild.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        isSubContractorSwitch = (Switch) findViewById(R.id.user_employee_activity_employee_details_sub_Switch);
        isSubContractorSwitch.setChecked(employee.getSubcontractorId() == null ? false : true);

        isSubContractorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton v, boolean isChecked) {
                isSubcontractorSwitched(isChecked);
            }
        });


        subcontractorNameButton = (Button) findViewById(R.id.user_employee_activity_employee_details_subcontractor_Button);
        subcontractorNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onSubcontractorNameButtonClicked();
            }
        });


        isActiveSwitch = (Switch) findViewById(R.id.user_employee_activity_employee_details_active_Switch);
        isActiveSwitch.setChecked(employee.isActive());


        isActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton v, boolean isChecked) {
                isActiveSwitched(isChecked);
            }
        });

        updateViews();
    }

    private void onSubcontractorNameButtonClicked() {
        TLApplicationContextProvider.getContext().setCurrentActivityObject(subcontractor);
        TLApplicationContextProvider.getContext().setCurrentReturnActivity(EmployeeDetails.class);

        Intent subcontractorDetails = new Intent(TLApplicationContextProvider.getContext(), SubcontractorDetails.class);
        startActivity(subcontractorDetails);
    }

    private void deleteClicked(final View v) {
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

    private void isSubcontractorSwitched(boolean isChecked) {
        TLUtil.hideKeyboard(activity);
        // do something, the isChecked will be

        if (!isChecked) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setMessage("Are you sure want to delete the employee from subcontractor?")
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateViews();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            employee.deleteFromSubcontractor(new TLJsonHttpResponseHandler(context) {
                                                                 @Override
                                                                 public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                                     Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                                                                     subcontractorNameButton.setVisibility(View.GONE);
                                                                     employee.setSubcontractorId(null);
                                                                     updateViews();
                                                                 }

                                                                 @Override
                                                                 public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                                                                     Log.e("EmployeeDetails", "error deleeting employee from subcontractor");
                                                                 }
                                                             }
                            );

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
//            intent to pick subcontractor      PickSubcontractorViewController
        }
    }


    private void isActiveSwitched(boolean isChecked) {
        TLUtil.hideKeyboard(activity);
        if (isChecked) {
            employee.activate(
                    new TLJsonHttpResponseHandler(context) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                            Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                            checkinsButton.setVisibility(View.VISIBLE);

                            try {
                                String activationCode = jsonResponse.getString("activation_code");
                                employee.setActivationCode(activationCode);
                            } catch (JSONException exception) {
                                Log.e("json exception", exception.toString());
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                    new TLJsonHttpResponseHandler(context) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                            Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                            checkinsButton.setVisibility(View.INVISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        updateViews();
    }

    private void updateViews() {

        if (employee.getActivationCode() == null) {
            isActiveSwitch.setChecked(false);
            checkinsButton.setVisibility(View.GONE);
        } else {
            isActiveSwitch.setChecked(true);
            checkinsButton.setVisibility(View.VISIBLE);
        }

        if (employee.getSubcontractorId() == null) {
            isSubContractorSwitch.setChecked(false);
            subcontractorNameButton.setVisibility(View.GONE);
        } else {
            isSubContractorSwitch.setChecked(true);
            subcontractorNameButton.setVisibility(View.VISIBLE);

            subcontractor = new TLSubcontractor(employee.getSubcontractorId());
            subcontractor.load(
                    new TLJsonHttpResponseHandler(context) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                            Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());
                            try {
                                Log.d("----------------------------", "setting subcontractor");
                                JSONObject subcontractorJson = jsonResponse.getJSONObject("subcontractor");
                                subcontractor.setId(subcontractorJson.getInt("id"));
                                subcontractor.setName(subcontractorJson.getString("name"));
                                if (subcontractorJson.getString("coi_expires_at") != null && !subcontractorJson.getString("coi_expires_at").equals("null")) {

                                    Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&", subcontractorJson.getString("coi_expires_at"));

                                    Date coi_expires_at = new DateTime(subcontractorJson.getString("coi_expires_at")).toDate();
                                    subcontractor.setCoiExpiresAt(coi_expires_at);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            subcontractorNameButton.setText(subcontractor.getName());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                            Log.e("Error loading sub", "error loading subcontractor");
                        }
                    });

        }

    }

    void saveButtonClicked(final View v) {
        TLUtil.hideKeyboard(activity);
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
//            employee.setSubcontractor(isSubContractorSwitch.isChecked());

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
