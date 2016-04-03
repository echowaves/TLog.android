package com.echowaves.tlog.controller.user.employee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.user.SignInActivity;
import com.echowaves.tlog.model.TLEmployee;
import com.echowaves.tlog.model.TLUser;

public class EmployeeDetails extends AppCompatActivity {

    private TLEmployee employee;


    private Button backButton;

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


        // show soft keyboard automagically
        nameTextFeild = (EditText) findViewById(R.id.user_employee_activity_employee_details_name_EditText);
        nameTextFeild.setText(employee.getName());
        emailTextField = (EditText) findViewById(R.id.user_employee_activity_employee_details_email_EditText);
        emailTextField.setText(employee.getEmail());
        saveButton = (Button) findViewById(R.id.user_employee_activity_employee_details_save_Button);

        nameTextFeild.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


//        private Switch subContractorSwitch;
//        private Switch isActiveSwitch;
//
//        private Button saveButton;
//        private Button actionCodesButton;


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
