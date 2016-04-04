package com.echowaves.tlog.controller.user.employee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLEmployee;

public class EmployeeActionCodes extends AppCompatActivity {

    private TLEmployee employee;


    private Button backButton;
    private TextView title;
    private EditText actionCodeTextField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_employee_activity_employee_action_codes);

        employee = (TLEmployee) TLApplicationContextProvider.getContext().getCurrentActivityObject();


        backButton = (Button) findViewById(R.id.user_employee_activity_employee_action_codes_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        title = (TextView) findViewById(R.id.user_employee_activity_employee_action_codes_title);
        title.setText(employee.getName());

        // show soft keyboard automagically
        actionCodeTextField = (EditText) findViewById(R.id.user_employee_activity_employee_action_codes_actionCode_EditText);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
