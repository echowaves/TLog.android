package com.echowaves.tlog.model;

import android.content.SharedPreferences;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dmitry on 3/24/16.
 */
public class TLUser extends TLObject {

    static String JWT_KEY = "TTLogJWT";
    static String ACTIVATION_CODE_KEY = "TTLogActivationCode";

    Integer id;
    String email;
    String password;

    public TLUser(Integer id, String email, String password ) {
        this.id = id;
        this.email = email;
        this.password = password;
    }


    public static void storeJwtLocally(String jwt) {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TLUser.JWT_KEY, jwt);
        editor.commit();
    }

    public static String retreiveJwtFromLocalStorage() {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());
        return prefs.getString(TLUser.JWT_KEY, "");
    }

    public static void clearJwtFromLocalStorage() {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(TLUser.JWT_KEY);
        editor.commit();
    }



    public void signIn(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("email", this.email);
        params.put("password", this.password);
        Header[] headers = new Header[0];

        HTTP_CLIENT.post(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/auth"),
                headers,
                params,
                JSON_CONTENT_TYPE,
                responseHandler);
    }




}
