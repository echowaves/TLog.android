package com.echowaves.tlog;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/25/14.
 */

public interface TLConstants {

    public static String API_VERSION = "1.0.0";


    public static final String TL_HOST = "http://app.tlog.us";
//        public static final String TL_HOST = "http://192.168.1.145:3000";
//        public static final String TL_HOST = "http://localhost:3000";
//        public static final String TL_HOST = "http://192.168.1.16:3000";
    public static final String TLAWSBucket = "http://staging-images.tlog.us";
    //    public static final String TLAWSBucket = "http://images.tlog.us";
//    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
//    public static SimpleDateFormat naturalDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    public static DateTimeFormatter defaultDateFormat = DateTimeFormat.forPattern("EEEE, MMMM d, yyyy, 'at' hh:mm a");
    public static DateTimeFormatter shortDateFormat = DateTimeFormat.forPattern("MMMM d, yyyy");
}
