package com.echowaves.tlog.model;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by dmitry on 4/18/16.
 */
public class TLReport extends TLObject {

    public static void yearsForUser(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/reports/years"),
                headers,
                new RequestParams(),
                responseHandler);
    }



    public static void monthsForUserAndYear(String year, JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/reports/months/" + year),
                headers,
                new RequestParams(),
                responseHandler);
    }


    public static void actionCodesDurationsByYearMonthForUser(String year, String month, JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/reports/action_codes/" + year + "/" + month),
                headers,
                new RequestParams(),
                responseHandler);
    }



    public static void employeesDurationsByYearMonthForUser(String year, String month, JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/reports/employees/" + year + "/" + month),
                headers,
                new RequestParams(),
                responseHandler);
    }

}
