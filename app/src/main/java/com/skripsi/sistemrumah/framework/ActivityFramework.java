package com.skripsi.sistemrumah.framework;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ActivityFramework extends AppCompatActivity {

    long mLastClickTime = 0;

    public static void preventMultiClick(final View view) {
        if (!view.isClickable()) {
            return;
        }
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 500);
    }

    protected Activity mActivity;
    protected TestUtils mTest = new TestUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSystem();
        initWindow();
    }

    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void initializeSystem() {
        mActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}