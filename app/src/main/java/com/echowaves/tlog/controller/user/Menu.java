package com.echowaves.tlog.controller.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;

public class Menu extends AppCompatActivity {

    private Button signoutButton;
    private Button employeesButton;
    private Button reportsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_menu);

        signoutButton = (Button) findViewById(R.id.menu_signOutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent signIn = new Intent(TLApplicationContextProvider.getContext(), SignIn.class);
        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        signIn.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(signIn);

        return;
    }
}
