package com.echowaves.tlog.controller.employee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.echowaves.tlog.R;

public class PickActionCode extends AppCompatActivity {
    private Button backButton;
    private Button checkoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_pick_action_code);

        backButton = (Button) findViewById(R.id.employee_activity_pick_action_code_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        checkoutButton = (Button) findViewById(R.id.employee_activity_pick_action_code_checkoutButton);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
