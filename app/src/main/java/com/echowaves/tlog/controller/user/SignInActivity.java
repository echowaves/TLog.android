package com.echowaves.tlog.controller.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;

public class SignInActivity extends AppCompatActivity {

    private EditText emailTextField;
    private EditText passwordTextFeild;
    private Button signInButton;
    private Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_sign_in);

        // show soft keyboard automagically
        emailTextField = (EditText) findViewById(R.id.signin_emailText);
        passwordTextFeild = (EditText) findViewById(R.id.signin_passwordText);
        signInButton = (Button) findViewById(R.id.signin_signinButton);
        signUpButton = (Button) findViewById(R.id.signin_signupButton);

        emailTextField.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);



        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent menu = new Intent(TLApplicationContextProvider.getContext(), MenuActivity.class);
                startActivity(menu);
            }
        });



        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent signUp = new Intent(TLApplicationContextProvider.getContext(), SignUpActivity.class);
                startActivity(signUp);
            }
        });


    }
}
