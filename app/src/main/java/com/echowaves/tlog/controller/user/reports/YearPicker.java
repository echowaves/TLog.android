package com.echowaves.tlog.controller.user.reports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.echowaves.tlog.R;

public class YearPicker extends AppCompatActivity {

    private Button backButton;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reports_activity_year_picker);

        backButton = (Button) findViewById(R.id.user_reports_activity_year_picker_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });


        listView = (ListView) findViewById(R.id.user_reports_activity_year_picker_listView);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
