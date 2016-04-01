package com.echowaves.tlog.model;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getSubcontractor() {
        return isSubcontractor;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public TLEmployee(Integer id, String name, String email, Boolean isSubcontractor, String activationCode) {
        this.id = id;
        this.name = name;

        this.email = email;
        this.isSubcontractor = isSubcontractor;
        this.activationCode = activationCode;
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

}
