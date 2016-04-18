package com.echowaves.tlog.controller.employee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.TLConstants;
import com.echowaves.tlog.model.TLCheckin;
import com.echowaves.tlog.model.TLEmployee;

import org.joda.time.DateTime;

public class CheckinDetails extends AppCompatActivity {

    private Button backButton;
    private Button deleteButton;

    private EditText checkInAtText;
    private EditText durationText;
    private EditText actionCodeText;


    TLCheckin checkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_checkin_details);

        checkin = (TLCheckin) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        backButton = (Button) findViewById(R.id.employee_activity_checkin_details_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        deleteButton = (Button) findViewById(R.id.employee_activity_checkin_details_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        checkInAtText = (EditText) findViewById(R.id.employee_activity_checkin_details_checkInAtText);
        checkInAtText.setText(new DateTime(checkin.getCheckedInAt()).toString(TLConstants.defaultDateFormat));

        durationText = (EditText) findViewById(R.id.employee_activity_checkin_details_durationText);
        durationText.setText(checkin.getDurationExtendedText());

        actionCodeText = (EditText) findViewById(R.id.employee_activity_checkin_details_actionCodeText);
        actionCodeText.setText(checkin.getActionCode().getCode() + ":" + checkin.getActionCode().getDescr());


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
