package com.augmentedrealityapp.delegates;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;


/**
 * Created by prasharma on 3/24/2017.
 */

public class ApplicationAugmented extends Application {

    private static ApplicationAugmented mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    public static ApplicationAugmented get() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance;
    }
}
