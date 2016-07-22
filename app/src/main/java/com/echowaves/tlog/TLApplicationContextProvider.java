package com.echowaves.tlog;

import android.content.Intent;
import android.util.Log;

import com.echowaves.tlog.controller.user.OutdatedVersion;
import com.echowaves.tlog.model.TLUser;
import com.echowaves.tlog.util.TLJsonHttpResponseHandler;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.localytics.android.LocalyticsActivityLifecycleCallbacks;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class TLApplicationContextProvider extends android.support.multidex.MultiDexApplication implements TLConstants {
    /**
     * Keeps a reference of the application context
     */
    private static TLApplicationContextProvider context;

    public TLApplicationContextProvider() {
        context = this;
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static TLApplicationContextProvider getContext() {
        if (context == null) {
            context = new TLApplicationContextProvider();
        }
        return context;
    }

    //this is used to pass parameters between intents
    private static Object currentActivityObject;

    public void setCurrentActivityObject(Object o) {
        currentActivityObject = o;
    }

    public Object getCurrentActivityObject() {
        return currentActivityObject;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        // Register LocalyticsActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(
                new LocalyticsActivityLifecycleCallbacks(this));

        Iconify.with(new FontAwesomeModule());

//        TLUser.checkApiVersion(new TLJsonHttpResponseHandler(context) {
//                                   @Override
//                                   public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
//                                       try {
//
//                                           String apiVersion = jsonResponse.getString("version");
//
////                                           if (!API_VERSION.equals(apiVersion)) {
////                                               Intent outdatedVersion = new Intent(TLApplicationContextProvider.getContext(), OutdatedVersion.class);
////                                               outdatedVersion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                               startActivity(outdatedVersion);
////
////                                           }
//
//                                       } catch (JSONException exception) {
//                                           Log.e(getClass().getName(), exception.toString());
//                                       }
//                                   }
//                               }
//        );
    }


}


//http://mutualmobile.com/posts/dex-64k-limit-not-problem-anymore-almost
//https://github.com/Kaopiz/android-segmented-control
//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//https://github.com/JoanZapata/android-iconify -- fontawesome
//https://developer.android.com/training/camera/photobasics.html