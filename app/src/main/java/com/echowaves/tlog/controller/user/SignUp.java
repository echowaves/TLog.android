package com.echowaves.tlog.controller.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;

public class SignUp extends AppCompatActivity {

    private Button backButton;
    private EditText emailTextField;
    private EditText passwordTextFeild;
    private EditText passwordConfirmTextFeild;
    private Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_sign_up);


        backButton = (Button) findViewById(R.id.signup_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                finish();
            }
        });

        // show soft keyboard automagically
        emailTextField = (EditText) findViewById(R.id.signup_emailText);
        passwordTextFeild = (EditText) findViewById(R.id.signup_passwordText);
        passwordConfirmTextFeild = (EditText) findViewById(R.id.signup_passwordConfirmText);
        signUpButton = (Button) findViewById(R.id.signup_signupButton);


        emailTextField.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent menu = new Intent(TLApplicationContextProvider.getContext(), Menu.class);
                startActivity(menu);
            }
        });


    }
}
