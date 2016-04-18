package com.echowaves.tlog.controller.user.reports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.echowaves.tlog.R;

public class YearPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reports_activity_year_picker);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
