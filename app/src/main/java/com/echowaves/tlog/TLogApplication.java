package com.echowaves.tlog;

import android.app.Application;
import com.localytics.android.LocalyticsActivityLifecycleCallbacks;


public class TLogApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Register LocalyticsActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(
                new LocalyticsActivityLifecycleCallbacks(this));
    }

}