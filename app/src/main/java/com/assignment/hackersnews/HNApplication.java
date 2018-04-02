package com.assignment.hackersnews;

import android.app.Application;
import android.support.annotation.VisibleForTesting;
//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Pankaj Kumar on 31/03/18.
 * EAT | DRINK | CODE
 */
public class HNApplication extends Application {

    @VisibleForTesting
    private String mTextForTest = null;

    @Override public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        mTextForTest = "HNApplication onCreate called";
    }

    @VisibleForTesting
    public String getTextForTest() {
        return mTextForTest;
    }
}