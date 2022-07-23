package com.skripsi.sistemrumah.framework;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class FragmentFramework extends Fragment {

    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSystem();
    }

    private void initializeSystem() {
        mActivity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}