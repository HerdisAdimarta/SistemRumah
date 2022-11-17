package com.skripsi.sistemrumah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skripsi.sistemrumah.R;
import com.skripsi.sistemrumah.framework.ActivityFramework;
import com.skripsi.sistemrumah.storage.SharedPreferencesProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends ActivityFramework {
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.llMain1)
    LinearLayout llMainMonitoring;
    @BindView(R.id.llMain2)
    LinearLayout llMainDaftarKartu;
    @BindView(R.id.llMain3)
    LinearLayout llMainUserTerdaftar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        initUser();
    }

    @OnClick(R.id.llMain1)
    public void llMain1(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, MonitoringActivity.class));
    }

    @OnClick(R.id.llMain2)
    public void llMain2(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, DaftarKartuActivity.class));
    }

    @OnClick(R.id.llMain3)
    public void llMain3(View view) {
        preventMultiClick(view);
        startActivity(new Intent(mActivity, UserTerdaftarActivity.class));
    }

    private void initUser() {
        tvUserName.setText(SharedPreferencesProvider.getInstance().get_pref_user_name(mActivity));
        Boolean isAdmin = SharedPreferencesProvider.getInstance().get_pref_is_admin(mActivity);
        if (!isAdmin) {
            llMainDaftarKartu.setVisibility(View.GONE);
            llMainUserTerdaftar.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.btnLogout)
    public void btnLogout(View view) {
        preventMultiClick(view);
        SharedPreferencesProvider.getInstance().clearSession(mActivity);
        startActivity(new Intent(mActivity, LoginActivity.class));
        finish();

    }


    @Override
    public void onBackPressed() {

    }

}