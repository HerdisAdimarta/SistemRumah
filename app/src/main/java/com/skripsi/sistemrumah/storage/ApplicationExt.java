package com.skripsi.sistemrumah.storage;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.skripsi.sistemrumah.api.rest.REST_Controller;

public class ApplicationExt  extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setUp(this);
    }


    public static void setUp(Context context) {
        REST_Controller.setUp();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


}
