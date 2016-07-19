package com.echowaves.tlog.controller.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.echowaves.tlog.R;
import com.localytics.android.Localytics;

public class OutdatedVersion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localytics.tagEvent("OutdatedVersion");

        setContentView(R.layout.user_activity_outdated_version);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

}
