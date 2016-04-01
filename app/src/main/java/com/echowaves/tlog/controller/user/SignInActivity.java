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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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
        emailTextField = (EditText) findViewById(R.id.user_activity_sign_in_emailText);
        passwordTextFeild = (EditText) findViewById(R.id.user_activity_sign_in_passwordText);
        signInButton = (Button) findViewById(R.id.user_activity_sign_in_signinButton);
        signUpButton = (Button) findViewById(R.id.user_activity_sign_in_signupButton);

        emailTextField.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);



        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                TLUser user = new TLUser(
                        null,
                        ((EditText) findViewById(R.id.user_activity_sign_in_emailText)).getText().toString(),
                        ((EditText) findViewById(R.id.user_activity_sign_in_passwordText)).getText().toString()
                );

                user.signIn(
                        new TLJsonHttpResponseHandler(v.getContext()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                Log.d(">>>>>>>>>>>>>>>>>>>> JSONResponse", jsonResponse.toString());

                                try {
                                    Log.d("token", jsonResponse.get("token").toString());
                                    TLUser.storeJwtLocally(jsonResponse.get("token").toString());


                                    Intent menu = new Intent(TLApplicationContextProvider.getContext(), MenuActivity.class);
                                    startActivity(menu);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder
                                        .setMessage("Unable to sign in, try again.")
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



            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent signUp = new Intent(TLApplicationContextProvider.getContext(), SignUpActivity.class);
                startActivity(signUp);
            }
        });




//TODO:
//        if(TLEmployee.retreiveActivationCodeFromLocalStorage() != nil) {
//            dispatch_async(dispatch_get_main_queue()){
//                self.performSegueWithIdentifier("employeeHomeSegue", sender: self)
//            }
//
//        } else {

            //auto sign in
            if(TLUser.retreiveJwtFromLocalStorage() != null) {
                Intent menu = new Intent(TLApplicationContextProvider.getContext(), MenuActivity.class);
                startActivity(menu);
            }
//        }



    }
}
