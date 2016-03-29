package com.echowaves.tlog.controller.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLUser;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.apache.commons.validator.GenericValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    private Button backButton;
    private EditText emailTextField;
    private EditText passwordTextField;
    private EditText passwordConfirmTextField;
    private Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_sign_up);


        backButton = (Button) findViewById(R.id.signup_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        // show soft keyboard automagically
        emailTextField = (EditText) findViewById(R.id.signup_emailText);
        passwordTextField = (EditText) findViewById(R.id.signup_passwordText);
        passwordConfirmTextField = (EditText) findViewById(R.id.signup_passwordConfirmText);
        signUpButton = (Button) findViewById(R.id.signup_signupButton);


        emailTextField.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                // validating code
                ArrayList<String> validationErrors = new ArrayList<>();
                if (GenericValidator.isBlankOrNull(emailTextField.getText().toString())) {
                    validationErrors.add("Email is required.");
                }
                if (GenericValidator.isBlankOrNull(passwordTextField.getText().toString())) {
                    validationErrors.add("Password is required.");
                }
                if (!GenericValidator.isEmail(emailTextField.getText().toString())) {
                    validationErrors.add("Wrong email format.");
                }
                if (!GenericValidator.minLength(passwordTextField.getText().toString(), 8)) {
                    validationErrors.add("Password must be 8 or more characters.");
                }
                if (!passwordTextField.getText().toString().equals(passwordConfirmTextField.getText().toString())) {
                    validationErrors.add("Password Confirm must match Password.");
                }
                if (!GenericValidator.maxLength(passwordTextField.getText().toString(), 50)) {
                    validationErrors.add("Password can't be longer than 50.");
                }
                if (!GenericValidator.maxLength(emailTextField.getText().toString(), 100)) {
                    validationErrors.add("Email can't be longer than 100.");
                }
                // validating code


                if (validationErrors.size() == 0) { // no validation errors, proceed

                    final TLUser user = new TLUser(
                            null,
                            ((EditText) findViewById(R.id.signup_emailText)).getText().toString(),
                            ((EditText) findViewById(R.id.signup_passwordText)).getText().toString()
                    );

                    user.signUp(
                            new TLJsonHttpResponseHandler(v.getContext()) {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {

                                    user.signIn(
                                            new TLJsonHttpResponseHandler(v.getContext()) {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                                    try {

                                                        Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());


                                                        Log.d("token", jsonResponse.get("token").toString());
                                                        TLUser.storeJwtLocally(jsonResponse.get("token").toString());


                                                        Intent menu = new Intent(TLApplicationContextProvider.getContext(), MenuActivity.class);
                                                        startActivity(menu);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }
                                    );
                                }


                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder
                                            .setMessage("Unable to sign up, perhaps user with this email already exists.")
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
                } else { // if validation errors
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
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
