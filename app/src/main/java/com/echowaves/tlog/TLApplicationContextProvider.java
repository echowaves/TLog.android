package com.echowaves.tlog;

import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.localytics.android.LocalyticsActivityLifecycleCallbacks;


public class TLApplicationContextProvider extends android.support.multidex.MultiDexApplication {
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

        // Register LocalyticsActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(
                new LocalyticsActivityLifecycleCallbacks(this));

        Iconify
                .with(new FontAwesomeModule());
    }

}


//http://mutualmobile.com/posts/dex-64k-limit-not-problem-anymore-almost
//https://github.com/Kaopiz/android-segmented-control
//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//https://github.com/JoanZapata/android-iconify -- fontawesome