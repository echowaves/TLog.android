package com.echowaves.tlog.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
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


    public void uploadCOI(File imageFile, JsonHttpResponseHandler responseHandler) throws FileNotFoundException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + TLUser.retreiveJwtFromLocalStorage());

        RequestParams params = new RequestParams();

        params.put("coi", imageFile, this.getId() + ".jpg");
        if(this.getCoiExpiresAt() == null) {
            params.put("coi_expires_at", new Date().toString());
        } else {
            params.put("coi_expires_at", this.getCoiExpiresAt().toString());
        }


        client.post(
                TLApplicationContextProvider.getContext(),
                getAbsoluteUrl("/subcontractors/" + this.getId().toString() + "/coi"),
                params,
                responseHandler);

    }

    public void downloadCOI(AsyncHttpResponseHandler responseHandler) {
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



    public static Bitmap scaleDown(byte[] bytes) { //}, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        Bitmap bitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        int maxSize = 2048;
        int height = 0;
        int width = 0;
        int inHeight = bitmap.getHeight();
        int inWidth = bitmap.getWidth();
        if(inWidth > inHeight) { // photo is landscape
            height =  (inHeight * maxSize) / inWidth;
            width = maxSize;
        } else { // photo is portrait
            height = maxSize;
            width =  (inWidth  * maxSize) / inHeight;
        }

        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, true);

        return bitmapResized;
    }

    public static Bitmap fixOrientation(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

}
