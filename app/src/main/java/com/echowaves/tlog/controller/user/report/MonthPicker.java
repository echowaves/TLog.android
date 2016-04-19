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
import com.echowaves.tlog.model.TLReport;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MonthPicker extends AppCompatActivity {
    private Context context;
    private String year;

    private Button backButton;
    private TextView title;

    private ListView listView;

    ArrayList<String> months;
    MonthPickerAdapter monthPickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_report_activity_month_picker);

        year = (String) TLApplicationContextProvider.getContext().getCurrentActivityObject();

        context = this;

        backButton = (Button) findViewById(R.id.user_report_activity_month_picker_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        title = (TextView) findViewById(R.id.user_report_activity_month_picker_titleText);
        title.setText(year);



        listView = (ListView) findViewById(R.id.user_report_activity_month_picker_listView);


        TLReport.monthsForUserAndYear(year, new TLJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                        try {

                            JSONArray jsonMonths = jsonResponse.getJSONArray("months");

                            months = new ArrayList<String>();

                            for (int i = 0; i < jsonMonths.length(); i++) {
                                String month = jsonMonths.getJSONObject(i).getString("date_part");

                                months.add(month);
                            }

                            monthPickerAdapter = new MonthPickerAdapter(context, months);
                            listView.setAdapter(monthPickerAdapter);

                        } catch (JSONException exception) {
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
