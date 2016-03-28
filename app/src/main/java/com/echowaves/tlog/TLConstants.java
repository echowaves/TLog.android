package com.echowaves.tlog;

import java.text.SimpleDateFormat;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/25/14.
 */

public interface TLConstants {

//    public static final String BASE_URL = "http://tlog.us:3000";
    public static final String BASE_URL = "http://192.168.1.145:3000";
//    public static final String BASE_URL = "http://172.20.10.2:3000";
    public static final String TLAWSBucket = "http://staging-images.tlog.us";
//    public static final String TLAWSBucket = "http://images.tlog.us";
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    public static SimpleDateFormat naturalDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


}
