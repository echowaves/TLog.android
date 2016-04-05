package com.echowaves.tlog.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by dmitry on 3/29/16.
 */
public class TLEmployee extends TLObject {

    private Integer id;
    private String name;
    private String email;
    private Boolean isSubcontractor;
    private String activationCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSubcontractor() {
        return isSubcontractor;
    }

    public void setSubcontractor(Boolean subcontractor) {
        isSubcontractor = subcontractor;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public Boolean isActive() {
        if (this.getActivationCode() == null || this.getActivationCode().equals("") || this.getActivationCode().equals("null")) {
            return false;
        }
        return true;
    }

    public TLEmployee(Integer id, String name, String email, Boolean isSubcontractor) {
        this.id = id;
        this.name = name;

        this.email = email;
        this.isSubcontractor = isSubcontractor;

    }

    public TLEmployee(Integer id, String name, String email, Boolean isSubcontractor, String activationCode) {
        this(id, name, email, isSubcontractor);
        this.activationCode = activationCode;
    }


    public static void storeActivationCodeLocally(String activationCode) {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TLUser.ACTIVATION_CODE_KEY, activationCode);
        editor.commit();

    }

    public static String retreiveActivationCodeFromLocalStorage() {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());
        return prefs.getString(TLUser.ACTIVATION_CODE_KEY, null);
    }

    public static void clearActivationCodeFromLocalStorage() {
        SharedPreferences prefs = new SecurePreferences(TLApplicationContextProvider.getContext());
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(TLUser.ACTIVATION_CODE_KEY);
        editor.commit();
    }


    public void create(JsonHttpResponseHandler responseHandler) {
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", this.getName());
            jsonParams.put("email", this.getEmail());
            jsonParams.put("is_subcontractor", this.getSubcontractor().toString());
            StringEntity entity = new StringEntity(jsonParams.toString());

            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/employees"),
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


    public void update(JsonHttpResponseHandler responseHandler) {
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", this.getName());
            jsonParams.put("email", this.getEmail());
            jsonParams.put("is_subcontractor", this.getSubcontractor().toString());
            StringEntity entity = new StringEntity(jsonParams.toString());

            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

            HTTP_CLIENT.put(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/employees/" + this.getId().toString()),
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

    public void delete(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.delete(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees/" + this.getId().toString()),
                headers,
                responseHandler);
    }

    public void activate(JsonHttpResponseHandler responseHandler) {
        try {
            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());
            JSONObject jsonParams = new JSONObject();
            StringEntity entity = new StringEntity(jsonParams.toString());

            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/employees/" + this.getId().toString() + "/activation"),
                    headers,
                    entity,
                    JSON_CONTENT_TYPE,
                    responseHandler);
        } catch (UnsupportedEncodingException e) {
            Log.e(this.getClass().getName(), e.toString());
        }
    }


    public void deactivate(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.delete(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees/" + this.getId().toString() + "/activation"),
                headers,
                responseHandler);
    }


    static public void loadAll(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees"),
                headers,
                new RequestParams(),
                responseHandler);
    }


    public void addActionCode(TLActionCode actionCode,
                              JsonHttpResponseHandler responseHandler) {
        try {
            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());
            JSONObject jsonParams = new JSONObject();
            StringEntity entity = new StringEntity(jsonParams.toString());

            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/employees/" + this.getId().toString() + "/actioncodes/" + actionCode.getId().toString()),
                    headers,
                    entity,
                    JSON_CONTENT_TYPE,
                    responseHandler);
        } catch (UnsupportedEncodingException e) {
            Log.e(this.getClass().getName(), e.toString());
        }
    }


    public void deleteActionCode(TLActionCode actionCode,
                                 JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.delete(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees/" + this.getId().toString() + "/actioncodes/" + actionCode.getId().toString()),
                headers,
                responseHandler);
    }

}
