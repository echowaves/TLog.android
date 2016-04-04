package com.echowaves.tlog.controller.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.user.employee.EmployeesActivity;
import com.echowaves.tlog.controller.user.reports.YearPickerActivity;
import com.echowaves.tlog.model.TLUser;

public class MenuActivity extends AppCompatActivity {

    private Button signoutButton;
    private Button employeesButton;
    private Button reportsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_menu);

        signoutButton = (Button) findViewById(R.id.user_activity_menu_signOutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        employeesButton = (Button) findViewById(R.id.user_activity_menu_employeesButton);
        employeesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent employees = new Intent(TLApplicationContextProvider.getContext(), EmployeesActivity.class);
                startActivity(employees);
            }
        });

        reportsButton = (Button) findViewById(R.id.user_activity_menu_reportsButton);
        reportsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent yearPicker = new Intent(TLApplicationContextProvider.getContext(), YearPickerActivity.class);
                startActivity(yearPicker);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        TLUser.clearJwtFromLocalStorage();

        Intent signIn = new Intent(TLApplicationContextProvider.getContext(), SignInActivity.class);
        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        signIn.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(signIn);

        return;
    }



}