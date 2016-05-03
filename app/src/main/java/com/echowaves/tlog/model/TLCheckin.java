package com.echowaves.tlog.model;

import android.util.Log;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by dmitry on 4/12/16.
 */
public class TLCheckin extends TLObject {

    private Integer id;
    private String email;
    private Integer userId;
    private Date checkedInAt;
    private Integer duration;
    private TLActionCode actionCode;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getUserId() {
        return userId;
    }

    public Date getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(Date checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationText() {
//            return (seconds / 3600, (seconds % 3600) / 60, (seconds % 3600) % 60)
            return (duration/3600) + ":" + (duration % 3600) / 60;
    }
    public String getDurationExtendedText() {
        return (duration/3600) + " hours : " + (duration % 3600) / 60 + " minutes";
    }



    public TLActionCode getActionCode() {
        return actionCode;
    }

    public TLCheckin(Date checkedInAt, TLActionCode actionCode) {
        this.checkedInAt = checkedInAt;
        this.actionCode = actionCode;
    }

    public TLCheckin(Integer id,
                     String email,
                     Integer userId,
                     Date checkedInAt,
                     Integer duration,
                     TLActionCode actionCode) {
        this.id = id;
        this.userId = userId;
        this.checkedInAt = checkedInAt;
        this.duration = duration;
        this.actionCode = actionCode;
    }


    public void create(JsonHttpResponseHandler responseHandler) {
        try {
            Header[] headers = new Header[1];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("checked_in_at", this.getCheckedInAt());
            jsonParams.put("action_code_id", this.getActionCode().getId());
            StringEntity entity = new StringEntity(jsonParams.toString());


            HTTP_CLIENT.post(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/employees/" +
                            TLEmployee.retreiveActivationCodeFromLocalStorage() +
                            "/checkins"),
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


    public void update(
            JsonHttpResponseHandler responseHandler) {

        try {
            Header[] headers = new Header[1];
            headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("checked_in_at", this.getCheckedInAt());
            jsonParams.put("action_code_id", this.getActionCode().getId());
            jsonParams.put("duration", this.getDuration());
            StringEntity entity = new StringEntity(jsonParams.toString());
            HTTP_CLIENT.put(
                    TLApplicationContextProvider.getContext(),
                    getAbsoluteUrl("/employees/" +
                            TLEmployee.retreiveActivationCodeFromLocalStorage() +
                            "/checkins/" +
                            this.getId()),
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


    public static void getAllCheckins(
            Integer pageNumber,
            Integer pageSize,
            JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);

        HTTP_CLIENT.get(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees/" +
                        TLEmployee.retreiveActivationCodeFromLocalStorage() +
                        "/checkins?page_number=" + pageNumber + "&page_size=" + pageSize),
                headers,
                new RequestParams(),
                responseHandler);
    }


    public void delete(
            JsonHttpResponseHandler responseHandler) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("Content-Type", JSON_CONTENT_TYPE);

        HTTP_CLIENT.delete(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/employees/" +
                        TLEmployee.retreiveActivationCodeFromLocalStorage() +
                        "/checkins/" + this.getId()),
                headers,
                responseHandler);
    }


}
