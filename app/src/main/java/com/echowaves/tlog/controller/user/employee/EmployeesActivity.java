package com.echowaves.tlog.controller.user.employee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.echowaves.tlog.R;

public class EmployeesActivity extends AppCompatActivity {

    private Button backButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_employee_activity_employees);

        backButton = (Button) findViewById(R.id.user_employee_employees_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        addButton = (Button) findViewById(R.id.user_employee_employees_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
