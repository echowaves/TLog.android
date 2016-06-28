package com.echowaves.tlog.controller.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.controller.user.employee.Employees;
import com.echowaves.tlog.controller.user.report.YearPicker;
import com.echowaves.tlog.controller.user.subcontractor.Subcontractors;
import com.echowaves.tlog.model.TLUser;
import com.localytics.android.Localytics;

public class Menu extends AppCompatActivity {

    private Button signoutButton;
    private Button employeesButton;
    private Button subcontractorsButton;
    private Button reportsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("Menu");

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
                Intent employees = new Intent(TLApplicationContextProvider.getContext(), Employees.class);
                startActivity(employees);
            }
        });

        subcontractorsButton= (Button) findViewById(R.id.user_activity_menu_subcontractorsButton);
        subcontractorsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent subcontractors = new Intent(TLApplicationContextProvider.getContext(), Subcontractors.class);
                startActivity(subcontractors);
            }
        });

        reportsButton = (Button) findViewById(R.id.user_activity_menu_reportsButton);
        reportsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent yearPicker = new Intent(TLApplicationContextProvider.getContext(), YearPicker.class);
                startActivity(yearPicker);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        TLUser.clearJwtFromLocalStorage();

        Intent signIn = new Intent(TLApplicationContextProvider.getContext(), SignIn.class);
        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        signIn.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(signIn);

        return;
    }



}
