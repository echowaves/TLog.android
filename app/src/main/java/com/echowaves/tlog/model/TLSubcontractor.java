package com.echowaves.tlog.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by dmitry on 3/29/16.
 */
public class TLSubcontractor extends TLObject {

    private Integer id;
    private String name;
    private Date coiExpiresAt;

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

    public Date getCoiExpiresAt() {
        return coiExpiresAt;
    }

    public void setCoiExpiresAt(Date coiExpiresAt) {
        this.coiExpiresAt = coiExpiresAt;
    }

    public TLSubcontractor(Integer id) {
        this.id = id;
    }


    public TLSubcontractor(Integer id, String name) {
        this(id);
        this.name = name;
    }

    public TLSubcontractor(Integer id, String name, Date coiExpiresAt) {
        this(id, name);
        this.coiExpiresAt = coiExpiresAt;
    }

    public void load(JsonHttpResponseHandler responseHandler) {
            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

            HTTP_CLIENT.get(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/subcontractors/" + this.getId().toString()),
                    headers,
                    new RequestParams(),
                    responseHandler);
    }


    public void create(JsonHttpResponseHandler responseHandler) {
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", this.getName());
            StringEntity entity = new StringEntity(jsonParams.toString());

            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/subcontractors"),
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
        if(this.coiExpiresAt == null) {
            this.coiExpiresAt = LocalDate.now().minus(Period.days(1)).toDate();
        }

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", this.getName());
            jsonParams.put("coi_expires_at", this.getCoiExpiresAt());
            StringEntity entity = new StringEntity(jsonParams.toString());

            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
            headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

            HTTP_CLIENT.put(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/subcontractors/" + this.getId().toString()),
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
                getAbsoluteUrl("/subcontractors/" + this.getId().toString()),
                headers,
                responseHandler);
    }


    static public void loadAll(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors"),
                headers,
                new RequestParams(),
                responseHandler);
    }


    public void loadEmployees(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors/" + this.getId().toString() + "/employees"),
                headers,
                new RequestParams(),
                responseHandler);
    }


    public void uploadCOI(Bitmap image, JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        entityBuilder.addBinaryBody("coi", byteArray, ContentType.create("image/png"), this.getId().toString() + ".png" );
//        entityBuilder.addTextBody("coi_expires_at", this.getCoiExpiresAt().toString() );

        HTTP_CLIENT.post(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors"),
                headers,
                entityBuilder.build(),
                JSON_CONTENT_TYPE,
                responseHandler);

    }

    public void downloadCOI(FileAsyncHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", "image/png");
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors/" + this.getId().toString() + "/coi"),
                headers,
                new RequestParams(),
                responseHandler);
    }


    public void deleteCOI(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.delete(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors/" + this.getId().toString() + "/coi"),
                headers,
                responseHandler);
    }


    public void hasCOI(JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);
        headers[1] = new BasicHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors/" + this.getId().toString() + "/coi_exists"),
                headers,
                new RequestParams(),
                responseHandler);
    }
    
}
