package com.echowaves.tlog.controller.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.echowaves.tlog.R;

public class OutdatedVersion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_outdated_version);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

}
