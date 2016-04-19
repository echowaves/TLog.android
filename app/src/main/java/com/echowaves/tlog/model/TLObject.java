package com.echowaves.tlog.model;

import android.app.ProgressDialog;

import com.echowaves.tlog.TLConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/18/14.
 */

public class TLObject implements TLConstants {

    protected final static AsyncHttpClient HTTP_CLIENT = new AsyncHttpClient();
    protected final static SyncHttpClient SYNC_HTTP_CLIENT = new SyncHttpClient();

    protected final static String JSON_CONTENT_TYPE = "application/json";

    private static ProgressDialog progressDialog = null;

    static {
//        PersistentCookieStore cookieStore = new PersistentCookieStore(TLApplicationContextProvider.getContext());
//        HTTP_CLIENT.setCookieStore(cookieStore);
//        SYNC_HTTP_CLIENT.setCookieStore(cookieStore);
        HTTP_CLIENT.setTimeout(60000);
        SYNC_HTTP_CLIENT.setTimeout(60000);
    }


    protected static String getAbsoluteUrl(String relativeUrl) {
        return TL_HOST + relativeUrl;
    }


    // this is a bit hacky, but it allows the object to store arbitrary values to be used with the ArrayAdapter
    private ArrayList<Object> values = new ArrayList<>();

    public void setValue(int i, Object o) {
        values.add(i, o);
    }
    public Object getValue(int i) {
        return values.get(i);
    }
}
