package com.echowaves.tlog.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * © Echowaves
 * Created by dmitry on 8/20/14.
 */
public class TLJsonHttpResponseHandler extends JsonHttpResponseHandler {
    private Context context;

    public TLJsonHttpResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    final public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
        if (headers != null) {
            for (Header h : headers) {
                Log.d("................ failed   key: ", h.getName());
                Log.d("................ failed value: ", h.getValue());
            }
        }
        if (responseBody != null) {
            Log.d("................ failed : ", responseBody);
        }
        if (error != null) {
            Log.d("................ failed error: ", error.toString());

            String msg = "";
            if (null != responseBody) {
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    msg = jsonResponse.getString("error");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                msg = error.getMessage();
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error1")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @Override
    final public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error2")
                .setMessage(errorResponse.toString())
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    final public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error3")
                .setMessage(errorResponse.toString())
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    final public void onStart() {
        super.onStart();
//        EWWave.showLoadingIndicator(context);
    }

    @Override
    final public void onFinish() {
        super.onFinish();
//        EWWave.hideLoadingIndicator();
    }


}
