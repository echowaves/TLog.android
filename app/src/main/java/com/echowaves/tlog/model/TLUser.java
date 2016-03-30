package com.echowaves.tlog.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by dmitry on 3/24/16.
 */
public class TLUser extends TLObject {

    static String JWT_KEY = "TTLogJWT";
    static String ACTIVATION_CODE_KEY = "TTLogActivationCode";

    Integer id;
    String email;
    String password;

    public TLUser(Integer id, String email, String password) {
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
        return prefs.getString(TLUser.JWT_KEY, null);
    }

    public static void clearJwtFromLocalStorage() {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(TLUser.JWT_KEY);
        editor.commit();
    }




    public void signIn(JsonHttpResponseHandler responseHandler) {
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email", this.email);
            jsonParams.put("password", this.password);
            StringEntity entity = new StringEntity(jsonParams.toString());

            Header[] headers = new Header[0];

            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/auth"),
                    headers,
                    entity,
                    JSON_CONTENT_TYPE,
                    responseHandler);

        } catch (JSONException jsonException) {
            Log.e(getClass().getName(), "jsonException", jsonException);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(getClass().getName(), "unsupportedEncoding Exception", unsupportedEncodingException);
        }
    }

    public void signUp(JsonHttpResponseHandler responseHandler) {
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email", this.email);
            jsonParams.put("password", this.password);
            StringEntity entity = new StringEntity(jsonParams.toString());

            Header[] headers = new Header[0];

            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/users"),
                    headers,
                    entity,
                    JSON_CONTENT_TYPE,
                    responseHandler);

        } catch (JSONException jsonException) {
            Log.e(getClass().getName(), "jsonException", jsonException);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(getClass().getName(), "unsupportedEncoding Exception", unsupportedEncodingException);
        }
    }


}
