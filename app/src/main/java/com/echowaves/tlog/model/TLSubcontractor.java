package com.echowaves.tlog.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.echowaves.tlog.TLApplicationContextProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

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

//
//    func delete(
//            success:() -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//
//        self.deleteCOI({
//                print("+++++++++++++++ deleted COI")
//        }, failure: { (error) in
//            print("+++++++++++++++ error deleting COI")
//        })
//
//        Alamofire.request(.DELETE, "\(TL_HOST)/subcontractors/\(self.id!)" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//                self.id = nil
//                success();
//                case .Failure(let error):
//                NSLog(error.description)
//                failure(error: error)
//            }
//        }
//    }
//
//
//
//    class func loadAll(
//    success:(allSubcontractors: [TLSubcontractor]) -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        Alamofire.request(.GET, "\(TL_HOST)/subcontractors" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//
//                let json = JSON(data: response.data!)["subcontractors"]
//
//                var allSubcontractors: [TLSubcontractor] = [TLSubcontractor]()
//
//                for (_,jsonSubcontractor):(String, JSON) in json {
//                    let subcontractor =
//                            TLSubcontractor(
//                                    id: jsonSubcontractor["id"].intValue,
//                            name: jsonSubcontractor["name"].stringValue)
//
//                    let coi_expires_at = jsonSubcontractor["coi_expires_at"].stringValue.toDate(DateFormat.ISO8601Format(.Extended))
//
//                    if(coi_expires_at != nil) {
//                        subcontractor.coiExpiresAt = coi_expires_at
//                    }
//
//                    allSubcontractors.append(subcontractor)
//
//                }
//
//                success(allSubcontractors: allSubcontractors);
//                case .Failure(let error):
//                NSLog(error.description)
//                failure(error: error)
//            }
//        }
//    }
//
//
//    func loadEmployees(
//            success:(employees: [TLEmployee]) -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        Alamofire.request(.GET, "\(TL_HOST)/subcontractors/\(self.id!)/employees" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//
//                let json = JSON(data: response.data!)["employees"]
//
//                var allEmployees: [TLEmployee] = [TLEmployee]()
//
//                for (_,jsonEmployee):(String, JSON) in json {
//                    let employee =
//                            TLEmployee(
//                                    id: jsonEmployee["id"].intValue,
//                            name: jsonEmployee["name"].stringValue,
//                            email: jsonEmployee["email"].stringValue)
//
//                    if(jsonEmployee["subcontractor_id"] != nil) {
//                        employee.subcontractorId = jsonEmployee["subcontractor_id"].intValue
//                    }
//
//
//                    allEmployees.append(employee)
//
//                }
//                success(employees: allEmployees);
//                case .Failure(let error):
//                NSLog(error.description)
//                failure(error: error)
//            }
//        }
//    }
//
//
//    func uploadCOI(image: UIImage,
//                   success:() -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//
//        Alamofire.upload(.POST,
//                "\(TL_HOST)/subcontractors/\(self.id!)/coi",
//                headers: headers,
//                multipartFormData: {
//            multipartFormData in
//            if let imageData = UIImagePNGRepresentation(TLSubcontractor.fixOrientation(image)) {
//                multipartFormData.appendBodyPart(data: imageData, name: "coi", fileName: "\(self.id!).png", mimeType: "image/png")
//            }
//
//            //                            multipartFormData.appendBodyPart(data: expiresAt.toString()!.dataUsingEncoding(NSUTF8StringEncoding)!, name: "coi_expires_at")
//
//        }, encodingCompletion: { encodingResult in
//            switch encodingResult {
//                case .Success(let upload, _, _):
//                upload.responseJSON { response in
//                    //                        debugPrint(response)
//                    success()
//                }
//                case .Failure(let error):
//                print(error)
//                failure(error: NSError(domain: "encoding", code: 0, userInfo: ["error":"error"]))
//            }
//        }
//        )
//    }
//
//
//    func downloadCOI(
//            success:(image: UIImage) -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "image/png"
//        ]
//        //        Request.addAcceptableImageContentTypes(["image/png"])
//
//        Alamofire.request(.GET,
//                "\(TL_HOST)/subcontractors/\(self.id!)/coi" , encoding: ParameterEncoding.JSON, headers: headers)
//        .responseImage { response in
//            //                debugPrint(response)
//
//            //                print(response.request)
//            //                print(response.response)
//            //                debugPrint(response.result)
//
//            if let image = response.result.value {
//                //                    print("image downloaded: \(image)")
//                success(image: image)
//            } else {
//                failure(error: NSError(domain: "image", code: 0, userInfo: ["download":"error"]))
//            }
//        }
//    }
//
//
//    func deleteCOI(
//            success:() -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        Alamofire.request(.DELETE,
//                "\(TL_HOST)/subcontractors/\(self.id!)/coi" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//                success()
//                case .Failure(let error):
//                failure(error: error)
//            }
//        }
//    }
//
//
//
//    func hasCOI(
//            success:() -> (),
//    failure:(error: NSError) -> ()) -> () {
//        let headers = [
//        "Authorization": "Bearer \(TLUser.retreiveJwtFromLocalStorage())",
//                "Content-Type": "application/json"
//        ]
//        //        Request.addAcceptableImageContentTypes(["image/png"])
//
//        Alamofire.request(.GET,
//                "\(TL_HOST)/subcontractors/\(self.id!)/coi_exists" , encoding: ParameterEncoding.JSON, headers: headers)
//        .validate(statusCode: 200..<300)
//        .responseJSON { response in
//            switch response.result {
//                case .Success:
//                print("~~~~~~~~~~~~~~~~~~~~ success \(response.response!.statusCode)")
//                success()
//                case .Failure(let error):
//                print("~~~~~~~~~~~~~~~~~~~~ failure \(response.response!.statusCode)")
//                failure(error: error)
//            }
//        }
//    }
//
//
//
//    class func fixOrientation(img:UIImage) -> UIImage {
//
//        if (img.imageOrientation == UIImageOrientation.Up) {
//            return img;
//        }
//
//        UIGraphicsBeginImageContextWithOptions(img.size, false, img.scale);
//        let rect = CGRect(x: 0, y: 0, width: img.size.width, height: img.size.height)
//        img.drawInRect(rect)
//
//        let normalizedImage : UIImage = UIGraphicsGetImageFromCurrentImageContext()
//        UIGraphicsEndImageContext();
//        return normalizedImage;
//
//    }


}
