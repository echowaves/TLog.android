package com.echowaves.tlog;

import android.app.Application;
import android.content.Context;

import com.localytics.android.LocalyticsActivityLifecycleCallbacks;


public class TLApplicationContextProvider extends Application
{
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
    public static Context getContext() {
        if (context == null) {
            context = new TLApplicationContextProvider();
        }
        return context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Register LocalyticsActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(
                new LocalyticsActivityLifecycleCallbacks(this));
    }

}