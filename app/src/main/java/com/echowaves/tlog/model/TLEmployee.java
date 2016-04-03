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

    public String getEmail() {
        return email;
    }

    public Boolean getSubcontractor() {
        return isSubcontractor;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public Boolean isActive() {
        if(this.getActivationCode() == null || this.getActivationCode().equals("") || this.getActivationCode().equals("null")) {
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

    public TLEmployee(Integer id, String name, String email, Boolean isSubcontractor, String activationCode ) {
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

//    func update(
//            success:() -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        let parameters = ["name": self.name!, "email": self.email!, "is_subcontractor": self.isSubcontractor.description]
//        Alamofire.request(.PUT, "\(TL_HOST)/employees/\(self.id!)" , parameters: parameters, encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//                success();
//                case .Failure(let error):
//                NSLog(error.description)
//                failure(error: error)
//            }
//        }
//    }
//
//

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
//
//
//    func activate(
//            success:(activationCode: String) -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        Alamofire.request(.POST, "\(TL_HOST)/employees/\(self.id!)/activation" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//                success(activationCode: response.result.value!["activation_code"] as! String);
//                case .Failure(let error):
//                NSLog(error.description)
//                failure(error: error)
//            }
//        }
//    }
//
//
//    func deactivate(
//            success:() -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        Alamofire.request(.DELETE, "\(TL_HOST)/employees/\(self.id!)/activation" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//                success();
//                case .Failure(let error):
//                NSLog(error.description)
//                failure(error: error)
//            }
//        }
//    }




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
