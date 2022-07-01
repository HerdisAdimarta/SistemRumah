package com.skripsi.sistemrumah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.framework.ActivityFramework;

public class SplashScreenActivity extends ActivityFramework {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        },4000);
    }
}