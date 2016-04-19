package com.echowaves.tlog.controller.user.report;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.TLApplicationContextProvider;
import com.echowaves.tlog.model.TLActionCode;
import com.echowaves.tlog.model.TLReport;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.joda.time.YearMonth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ActionCodesReport extends AppCompatActivity {
    private Context context;
    private String year;
    private String month;

    private Button backButton;
    private TextView title;

    private ListView listView;

    ArrayList<TLActionCode> actionCodes;
    ActionCodesReportAdapter actionCodesReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_report_activity_action_codes_report);

        year = ((String[]) TLApplicationContextProvider.getContext().getCurrentActivityObject())[0];
        month = ((String[]) TLApplicationContextProvider.getContext().getCurrentActivityObject())[1];

        context = this;

        backButton = (Button) findViewById(R.id.user_report_activity_action_codes_report_backButton);
        backButton.setText("< " + year);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        YearMonth jodaDate = new YearMonth(new Integer(year).intValue(), new Integer(month).intValue());
        title = (TextView) findViewById(R.id.user_report_activity_action_codes_report_titleText);
        title.setText(jodaDate.toString("MMMM"));


        listView = (ListView) findViewById(R.id.user_report_activity_action_codes_report_listView);


        TLReport.actionCodesDurationsByYearMonthForUser(year, month, new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {

                            JSONArray jsonValues = jsonResponse.getJSONArray("action_codes");

                            actionCodes = new ArrayList<TLActionCode>();

                            for (int i = 0; i < jsonValues.length(); i++) {
                                TLActionCode actionCode =
                                        new TLActionCode(null,
                                                jsonValues.getJSONObject(i).getString("code"),
                                                jsonValues.getJSONObject(i).getString("description")
                                        );

                                actionCode.setValue(0, jsonValues.getJSONObject(i).getString("sum"));
                                actionCodes.add(actionCode);
                            }


                            actionCodesReportAdapter = new ActionCodesReportAdapter(year, month, context, actionCodes);
                            listView.setAdapter(actionCodesReportAdapter);

                        } catch (
                                JSONException exception
                                )

                        {
                            Log.e(getClass().getName(), exception.toString());
                        }
                    }
                }

        );

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
