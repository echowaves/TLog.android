package com.echowaves.tlog.model;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by dmitry on 3/29/16.
 */
public class TLActionCode extends TLObject {

    private Integer id;
    private String code;
    private String descr;

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescr() {
        return descr;
    }

    public TLActionCode(Integer id, String code, String descr) {
        this.id = id;
        this.code = code;
        this.descr = descr;
    }


    public static void allActionCodesForEmployee(
            TLEmployee employee,
            JsonHttpResponseHandler responseHandler) {

        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees/" + employee.getId().toString() + "/actioncodes"),
                headers,
                new RequestParams(),
                responseHandler);
    }


}
